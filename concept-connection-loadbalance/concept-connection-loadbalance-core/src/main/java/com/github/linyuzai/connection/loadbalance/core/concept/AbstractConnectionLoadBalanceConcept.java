package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.*;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    protected final Map<Object, Connection> connections = new ConcurrentHashMap<>();

    protected final ConnectionServerProvider connectionServerProvider;

    protected final ConnectionProxy connectionProxy;

    protected final List<ConnectionFactory> connectionFactories;

    protected final List<ConnectionSelector> connectionSelectors;

    protected final List<MessageFactory> messageFactories;

    protected final ConnectionEventPublisher eventPublisher;

    public AbstractConnectionLoadBalanceConcept(ConnectionServerProvider connectionServerProvider,
                                                ConnectionProxy connectionProxy,
                                                List<ConnectionFactory> connectionFactories,
                                                List<ConnectionSelector> connectionSelectors,
                                                List<MessageFactory> messageFactories,
                                                ConnectionEventPublisher eventPublisher) {
        this.connectionServerProvider = connectionServerProvider;
        this.connectionProxy = connectionProxy;
        this.connectionFactories = connectionFactories;
        this.connectionSelectors = connectionSelectors;
        this.messageFactories = messageFactories;
        this.eventPublisher = eventPublisher;
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
    public Connection create(Object o, Map<String, String> metadata) {
        ConnectionFactory factory = getConnectionFactory(o, metadata);
        if (factory == null) {
            throw new ConnectionLoadBalanceException("No MessageFactory available with " + o);
        }
        Connection connection = factory.create(o, metadata);
        if (connection == null) {
            throw new ConnectionLoadBalanceException("Message can not be created with " + o);
        }
        return connection;
    }

    @Override
    public void open(Object o, Map<String, String> metadata) {
        open(create(o, metadata));
    }

    @Override
    public void open(Connection connection) {
        connections.put(connection.getId(), connection);
        if (connection.hasProxyFlag()) {
            publish(new ProxyConnectionAddedEvent(connection));
        } else {
            publish(new ConnectionOpenEvent(connection));
        }
    }

    public ConnectionFactory getConnectionFactory(Object con, Map<String, String> metadata) {
        for (ConnectionFactory connectionFactory : connectionFactories) {
            if (connectionFactory.support(con, metadata)) {
                return connectionFactory;
            }
        }
        return null;
    }

    @Override
    public void close(Object id) {
        Connection connection = connections.remove(id);
        if (connection == null) {
            return;
        }
        if (connection.hasProxyFlag()) {
            publish(new ProxyConnectionRemovedEvent(connection));
        } else {
            publish(new ConnectionCloseEvent(connection));
        }
    }

    @Override
    public void message(Object id, byte[] message) {
        Connection connection = getConnection(id);
        if (connection == null) {
            publish(new UnknownMessageEvent(id, message));
        } else {
            MessageDecoder decoder = connection.getMessageDecoder();
            Message decode = decoder.decode(message);
            if (connection.hasProxyFlag()) {
                if (decode.hasProxyFlag()) {
                    publish(new ProxyMessageReceivedEvent(connection, decode));
                    //反向连接
                    proxyOnMessage(decode);
                } else {
                    //代理消息转发
                    send(decode);
                }
            } else {
                publish(new MessageReceiveEvent(connection, decode));
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
            Connection connection = connectionProxy.proxy(server, this);
            publish(new ConnectionProxyEvent(connection, server));
            open(connection);
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
        Connection proxy = connectionProxy.proxy(message.getPayload(), this);
        open(proxy);
    }

    public boolean containsProxyConnection(String instanceId) {
        if (instanceId == null || instanceId.isEmpty()) {
            return false;
        }
        for (Connection connection : connections.values()) {
            if (connection.hasProxyFlag()) {
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
        Connection connection = getConnection(id);
        if (connection == null) {
            publish(new UnknownErrorEvent(id, e));
        } else {
            publish(new ConnectionErrorEvent(connection, e));
        }
    }

    @Override
    public Connection getConnection(Object id) {
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
            publish(new DeadMessageEvent(message));
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
        if (message.hasProxyFlag()) {
            publish(new ProxyMessageSentEvent(connection, message));
        } else {
            publish(new MessageSendEvent(connection, message));
        }
    }

    @Override
    public void send(Object msg, Map<String, String> headers) {
        if (headers == null) {
            send(msg);
            return;
        }
        Message message = createMessage(msg);
        message.getHeaders().putAll(headers);
        send(message);
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

    @Override
    public void publish(Object event) {
        eventPublisher.publish(event);
    }

    @SuppressWarnings("unchecked")
    public static class AbstractBuilder<T extends AbstractBuilder<T>> {

        protected ConnectionServerProvider connectionServerProvider;

        protected ConnectionProxy connectionProxy;

        protected List<ConnectionFactory> connectionFactories = new ArrayList<>();

        protected List<ConnectionSelector> connectionSelectors = new ArrayList<>();

        protected List<MessageFactory> messageFactories = new ArrayList<>();

        protected ConnectionEventPublisher eventPublisher;

        public T connectionServerProvider(ConnectionServerProvider provider) {
            this.connectionServerProvider = provider;
            return (T) this;
        }

        public T connectionProxy(ConnectionProxy proxy) {
            this.connectionProxy = proxy;
            return (T) this;
        }

        public T eventPublisher(ConnectionEventPublisher publisher) {
            this.eventPublisher = publisher;
            return (T) this;
        }

        public T addConnectionFactory(ConnectionFactory factory) {
            return addConnectionFactories(factory);
        }

        public T addConnectionFactories(ConnectionFactory... factories) {
            return addConnectionFactories(Arrays.asList(factories));
        }

        public T addConnectionFactories(Collection<? extends ConnectionFactory> factories) {
            this.connectionFactories.addAll(factories);
            return (T) this;
        }

        public T addConnectionSelector(ConnectionSelector selector) {
            return addConnectionSelectors(selector);
        }

        public T addConnectionSelectors(ConnectionSelector... selectors) {
            return addConnectionSelectors(Arrays.asList(selectors));
        }

        public T addConnectionSelectors(Collection<? extends ConnectionSelector> selectors) {
            this.connectionSelectors.addAll(selectors);
            return (T) this;
        }

        public T addMessageFactory(MessageFactory factory) {
            return addMessageFactories(factory);
        }

        public T addMessageFactories(MessageFactory... factories) {
            return addMessageFactories(Arrays.asList(factories));
        }

        public T addMessageFactories(Collection<? extends MessageFactory> factories) {
            this.messageFactories.addAll(factories);
            return (T) this;
        }

        protected void preBuild() {
            if (connectionServerProvider == null) {
                throw new ConnectionLoadBalanceException("ConnectionServerProvider is null");
            }
            if (connectionProxy == null) {
                throw new ConnectionLoadBalanceException("ConnectionProxy is null");
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

            messageFactories.add(new ObjectMessageFactory());

            if (eventPublisher == null) {
                eventPublisher = new DefaultConnectionEventPublisher();
            }
        }
    }
}
