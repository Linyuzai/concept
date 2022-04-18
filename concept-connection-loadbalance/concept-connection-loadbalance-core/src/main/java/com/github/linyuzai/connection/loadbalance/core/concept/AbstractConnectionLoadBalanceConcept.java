package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactoryAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelectorAdapter;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    private final Map<Object, Connection> connections = new ConcurrentHashMap<>();

    private final ConnectionServerProvider connectionServerProvider;

    private final ConnectionProxy connectionProxy;

    private final MessageFactoryAdapter messageFactoryAdapter;

    private final MessageEncoder messageEncoder;

    private final MessageDecoder messageDecoder;

    private final ConnectionSelectorAdapter connectionSelectorAdapter;

    public AbstractConnectionLoadBalanceConcept(ConnectionServerProvider connectionServerProvider,
                                                ConnectionProxy connectionProxy,
                                                ConnectionSelectorAdapter connectionSelectorAdapter,
                                                MessageFactoryAdapter messageFactoryAdapter,
                                                MessageEncoder messageEncoder,
                                                MessageDecoder messageDecoder) {
        this.connectionServerProvider = applyAware(connectionServerProvider);
        this.connectionProxy = applyAware(connectionProxy);
        this.connectionSelectorAdapter = applyAware(connectionSelectorAdapter);
        this.messageFactoryAdapter = applyAware(messageFactoryAdapter);
        this.messageEncoder = applyAware(messageEncoder);
        this.messageDecoder = applyAware(messageDecoder);
    }

    private <T> T applyAware(T o) {
        if (o instanceof ConnectionLoadBalanceConceptAware) {
            ((ConnectionLoadBalanceConceptAware) o).setConnectionLoadBalanceConcept(this);
        }
        return o;
    }

    @Override
    public void initialize() {
        List<ConnectionServer> servers = connectionServerProvider.getConnectionServers();
        ConnectionServer client = connectionServerProvider.getClient();
        for (ConnectionServer server : servers) {
            Connection connection = connectionProxy.proxy(server);
            add(connection);
            connection.send(createMessage(client));
        }
    }

    @Override
    public void destroy() {
        for (Connection connection : connections.values()) {
            try {
                connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    @Override
    public void add(Connection connection) {
        connections.put(connection.getId(), applyAware(connection));
    }

    @Override
    public void remove(Object id) {
        Connection connection = connections.remove(id);

    }

    @Override
    public void message(Object id, byte[] message) {
        Message decode = messageDecoder.decode(message);
        Connection connection = get(id);
        if (connection == null) {
            //TODO
        } else {
            if (connection.isProxy()) {
                if (decode.isProxy()) {
                    if (hasProxyConnection(decode)) {
                        //已经存在对应的服务连接
                        return;
                    }
                    //TODO 反向连接
                    connectionProxy.proxy(decode.getPayload());
                } else {
                    //TODO 代理消息转发
                    send(decode);
                }
            } else {
                //TODO 前端消息上来
            }
        }
    }

    public boolean hasProxyConnection(Message message) {
        String instanceId = message.getHeaders().get(ConnectionServer.INSTANCE_ID);
        if (instanceId == null || instanceId.isEmpty()) {
            return false;
        }
        for (Connection connection : connections.values()) {
            if (connection.isProxy()) {
                String exist = connection.getMetadata().get(ConnectionServer.INSTANCE_ID);
                if (instanceId.equals(exist)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void error(Object id, Throwable e) {
        Connection connection = get(id);
        if (connection == null) {

        } else {

        }
    }

    @Override
    public Connection get(Object id) {
        return connections.get(id);
    }

    @Override
    public void send(Object msg) {
        Message message = createMessage(msg);
        ConnectionSelector selector = connectionSelectorAdapter.getConnectionSelector(message);
        Connection connection;
        List<Connection> list = new ArrayList<>(connections.values());
        if (selector == null) {
            if (list.size() > 1) {
                connection = new Connections(list);
            } else if (list.size() == 1) {
                connection = list.get(0);
            } else {
                return;
            }
        } else {
            connection = selector.select(message, list);
        }
        //添加转发标记，防止其他服务再次转发
        message.getHeaders().put("forward", "");
        connection.send(message);
    }

    public Message createMessage(Object msg) {
        if (msg instanceof Message) {
            return (Message) msg;
        }
        MessageFactory messageFactory = messageFactoryAdapter.getMessageFactory(msg);
        return messageFactory.create(msg);
    }
}
