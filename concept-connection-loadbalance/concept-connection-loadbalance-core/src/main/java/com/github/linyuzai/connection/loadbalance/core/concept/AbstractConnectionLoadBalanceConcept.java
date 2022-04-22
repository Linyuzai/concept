package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.*;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    protected final Map<Object, Connection> clientConnections = new ConcurrentHashMap<>();

    protected final Map<Object, Connection> subscriberConnections = new ConcurrentHashMap<>();

    protected final Map<Object, Connection> observableConnections = new ConcurrentHashMap<>();

    protected final ConnectionServerProvider connectionServerProvider;

    protected final ConnectionSubscriber connectionSubscriber;

    protected final List<ConnectionFactory> connectionFactories;

    protected final List<ConnectionSelector> connectionSelectors;

    protected final List<MessageFactory> messageFactories;

    protected final ConnectionEventPublisher eventPublisher;

    public AbstractConnectionLoadBalanceConcept(ConnectionServerProvider connectionServerProvider,
                                                ConnectionSubscriber connectionSubscriber,
                                                List<ConnectionFactory> connectionFactories,
                                                List<ConnectionSelector> connectionSelectors,
                                                List<MessageFactory> messageFactories,
                                                ConnectionEventPublisher eventPublisher) {
        this.connectionServerProvider = connectionServerProvider;
        this.connectionSubscriber = connectionSubscriber;
        this.connectionFactories = connectionFactories;
        this.connectionSelectors = connectionSelectors;
        this.messageFactories = messageFactories;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void initialize() {
        subscribe();
    }

    @Override
    public void destroy() {
        for (Connection.Type type : Connection.Type.values()) {
            Map<Object, Connection> connections = getConnections(type);
            for (Connection connection : connections.values()) {
                try {
                    connection.close();
                } catch (Throwable ignore) {
                }
            }
        }
    }

    @Override
    public Connection create(Object o, Map<Object, Object> metadata) {
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
    public Connection open(Object o, Map<Object, Object> metadata, Connection.Type type) {
        Connection connection = create(o, metadata);
        open(connection, type);
        return connection;
    }

    @Override
    public void open(Connection connection, Connection.Type type) {
        switch (type) {
            case CLIENT:
                clientConnections.put(connection.getId(), connection);
                publish(new ConnectionOpenEvent(connection));
            case SUBSCRIBER:
                subscriberConnections.put(connection.getId(), connection);
                //TODO
                //publish(new ProxyConnectionAddedEvent(connection));
            case OBSERVABLE:
                observableConnections.put(connection.getId(), connection);
                //TODO
                //publish(new ProxyConnectionAddedEvent(connection));
        }
    }

    public ConnectionFactory getConnectionFactory(Object con, Map<Object, Object> metadata) {
        for (ConnectionFactory connectionFactory : connectionFactories) {
            if (connectionFactory.support(con, metadata)) {
                return connectionFactory;
            }
        }
        return null;
    }

    @Override
    public void close(Object id, Connection.Type type) {
        Connection connection = getConnections0(type).remove(id);
        if (connection == null) {
            //UnknownConnectionClose
            return;
        }
        if (connection.hasProxyFlag()) {
            publish(new ProxyConnectionCloseEvent(connection));
        } else {
            publish(new ConnectionCloseEvent(connection));
        }
    }

    @Override
    public void message(Object id, byte[] message, Connection.Type type) {
        Connection connection = getConnection(id, type);
        if (connection == null) {
            publish(new UnknownMessageEvent(id, message));
        } else {
            MessageDecoder decoder = connection.getMessageDecoder();
            Message decode = decoder.decode(message);
            switch (type) {
                case CLIENT:
                    publish(new MessageReceiveEvent(connection, decode));
                    break;
                case SUBSCRIBER:
                    //转发
                    send(decode);
                    break;
                case OBSERVABLE:
                    //TODO
                    //publish(new ProxyMessageReceiveEvent(connection, decode));
                    //反向连接
                    subscribe(decode.getPayload(), false);
                    break;
            }
        }
    }

    public void subscribe() {
        List<ConnectionServer> servers = connectionServerProvider.getConnectionServers();
        for (ConnectionServer server : servers) {
            subscribe(server, true);
        }
    }

    public void subscribe(ConnectionServer server, boolean reply) {
        if (containsSubscriberConnection(server)) {
            //已经存在对应的服务连接
            return;
        }
        Connection subscriber = connectionSubscriber.subscribe(server, this);
        if (subscriber == null) {
            publish(new ProxyConnectionOpenErrorEvent(server));
            return;
        }
        open(subscriber, Connection.Type.SUBSCRIBER);
        publish(new ProxyConnectionOpenEvent(subscriber, server));
        if (reply) {
            subscriber.send(createMessage(connectionServerProvider.getClient()));
        }
    }

    public boolean containsSubscriberConnection(ConnectionServer server) {
        if (server == null) {
            return false;
        }
        for (Connection connection : subscriberConnections.values()) {
            ConnectionServer exist = (ConnectionServer) connection.getMetadata()
                    .get(ConnectionServer.class);
            if (server.getInstanceId().equals(exist.getInstanceId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void error(Object id, Throwable e, Connection.Type type) {
        Connection connection = getConnection(id, type);
        if (connection == null) {
            publish(new UnknownErrorEvent(id, e));
        } else {
            publish(new ConnectionErrorEvent(connection, e));
        }
    }

    @Override
    public Connection getConnection(Object id, Connection.Type type) {
        return getConnections0(type).get(id);
    }

    @Override
    public Map<Object, Connection> getConnections(Connection.Type type) {
        return Collections.unmodifiableMap(getConnections0(type));
    }

    private Map<Object, Connection> getConnections0(Connection.Type type) {
        switch (type) {
            case CLIENT:
                return clientConnections;
            case SUBSCRIBER:
                return subscriberConnections;
            case OBSERVABLE:
                return observableConnections;
            default:
                return Collections.emptyMap();
        }
    }

    @Override
    public void send(Object msg) {
        Message message = createMessage(msg);
        ConnectionSelector selector = getConnectionSelector(message);
        Connection connection;
        List<Connection> clients = new ArrayList<>(clientConnections.values());
        List<Connection> observables = new ArrayList<>(observableConnections.values());
        if (selector == null) {
            connection = Connections.of(clients, observables);
        } else {
            connection = selector.select(message, clients, observables);
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
            publish(new ProxyMessageSendEvent(connection, message));
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

        protected ConnectionSubscriber connectionSubscriber;

        protected List<ConnectionFactory> connectionFactories = new ArrayList<>();

        protected List<ConnectionSelector> connectionSelectors = new ArrayList<>();

        protected List<MessageFactory> messageFactories = new ArrayList<>();

        protected ConnectionEventPublisher eventPublisher;

        public T connectionServerProvider(ConnectionServerProvider provider) {
            this.connectionServerProvider = provider;
            return (T) this;
        }

        public T connectionProxy(ConnectionSubscriber proxy) {
            this.connectionSubscriber = proxy;
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
            if (connectionSubscriber == null) {
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
