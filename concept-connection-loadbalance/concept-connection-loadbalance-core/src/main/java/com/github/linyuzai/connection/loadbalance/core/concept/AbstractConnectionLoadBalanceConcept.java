package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.proxy.ProxyConnectionMessageFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
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

    private final MessageEncoder messageEncoder;

    private final MessageDecoder messageDecoder;

    private final List<MessageFactory> messageFactories;

    private final List<ConnectionSelector> connectionSelectors;

    public AbstractConnectionLoadBalanceConcept(ConnectionServerProvider connectionServerProvider,
                                                ConnectionProxy connectionProxy,
                                                MessageEncoder messageEncoder,
                                                MessageDecoder messageDecoder,
                                                List<MessageFactory> messageFactories,
                                                List<ConnectionSelector> connectionSelectors) {
        this.connectionServerProvider = applyAware(connectionServerProvider);
        this.connectionProxy = applyAware(connectionProxy);
        this.messageEncoder = applyAware(messageEncoder);
        this.messageDecoder = applyAware(messageDecoder);
        this.messageFactories = applyAware(messageFactories);
        this.connectionSelectors = applyAware(connectionSelectors);
    }

    private <T> T applyAware(T o) {
        if (o instanceof ConnectionLoadBalanceConceptAware) {
            ((ConnectionLoadBalanceConceptAware) o).setConnectionLoadBalanceConcept(this);
        } else if (o instanceof Collection) {
            ((Collection<?>) o).forEach(this::applyAware);
        }
        return o;
    }

    @Override
    public void initialize() {
        proxyOnServer(true);
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
                    //反向连接
                    proxyOnMessage(decode);
                } else {
                    //代理消息转发
                    send(decode);
                }
            } else {
                //TODO 前端消息上来
            }
        }
    }

    public void proxyOnServer(boolean sendMessage) {
        List<ConnectionServer> servers = connectionServerProvider.getConnectionServers();
        ConnectionServer client = connectionServerProvider.getClient();
        for (ConnectionServer server : servers) {
            if (containsProxyConnection(server.getInstanceId())) {
                continue;
            }
            Connection connection = connectionProxy.proxy(server);
            add(connection);
            if (sendMessage) {
                connection.send(createMessage(client));
            }
        }
    }

    public void proxyOnMessage(Message message) {
        String instanceId = message.getHeaders().get(ConnectionServer.INSTANCE_ID);
        if (containsProxyConnection(instanceId)) {
            //已经存在对应的服务连接
            return;
        }
        Connection proxy = connectionProxy.proxy(message.getPayload());
        add(proxy);
    }

    public boolean containsProxyConnection(String instanceId) {
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
        ConnectionSelector selector = getConnectionSelector(message);
        Connection connection;
        List<Connection> list = new ArrayList<>(connections.values());
        if (selector == null) {
            connection = Connections.of(list);
        } else {
            connection = selector.select(message, list);
        }
        if (connection == null) {
            return;
        }
        //添加转发标记，防止其他服务再次转发
        String forward = message.getHeaders().get(Message.FORWARD);
        String instanceId = connectionServerProvider.getClient().getInstanceId();
        String newForward;
        if (forward == null) {
            newForward = instanceId;
        } else {
            newForward = forward + " > " + instanceId;
        }
        message.getHeaders().put(Message.FORWARD, newForward);
        connection.send(message);
    }

    public ConnectionSelector getConnectionSelector(Message message) {
        for (ConnectionSelector connectionSelector : connectionSelectors) {
            if (connectionSelector.support(message)) {
                return connectionSelector;
            }
        }
        return null;
    }

    public Message createMessage(Object msg) {
        if (msg instanceof Message) {
            return (Message) msg;
        }
        MessageFactory messageFactory = getMessageFactory(msg);
        if (messageFactory == null) {
            throw new ConnectionLoadBalanceException("No MessageFactory available with " + msg);
        }
        Message message = messageFactory.create(msg);
        if (message == null) {
            throw new ConnectionLoadBalanceException("Message can not be created with " + msg);
        }
        return message;
    }

    public MessageFactory getMessageFactory(Object msg) {
        for (MessageFactory messageFactory : messageFactories) {
            if (messageFactory.support(msg)) {
                return messageFactory;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static class AbstractBuilder<T extends AbstractBuilder<T>> {

        protected ConnectionServerProvider connectionServerProvider;

        protected ConnectionProxy connectionProxy;

        protected MessageEncoder messageEncoder;

        protected MessageDecoder messageDecoder;

        protected List<MessageFactory> messageFactories = new ArrayList<>();

        protected List<ConnectionSelector> connectionSelectors = new ArrayList<>();

        public T connectionServerProvider(ConnectionServerProvider connectionServerProvider) {
            this.connectionServerProvider = connectionServerProvider;
            return (T) this;
        }

        public void preBuild() {
            if (connectionServerProvider == null) {
                throw new ConnectionLoadBalanceException("ConnectionServerProvider is null");
            }
            if (connectionProxy == null) {
                throw new ConnectionLoadBalanceException("ConnectionProxy is null");
            }
            if (messageEncoder == null) {

            }
            if (messageDecoder == null) {

            }

            boolean containsProxyConnectionMessageFactory = false;
            for (MessageFactory messageFactory : messageFactories) {
                if (messageFactory instanceof ProxyConnectionMessageFactory) {
                    containsProxyConnectionMessageFactory = true;
                    break;
                }
            }
            if (!containsProxyConnectionMessageFactory) {
                messageFactories.add(0, new ProxyConnectionMessageFactory());
            }
        }
    }
}
