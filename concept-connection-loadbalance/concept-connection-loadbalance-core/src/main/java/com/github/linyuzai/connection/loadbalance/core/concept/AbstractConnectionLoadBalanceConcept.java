package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.exception.NoConnectionTypeException;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessageFactory;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.*;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Getter
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    protected final Map<String, Map<Object, Connection>> connections = new ConcurrentHashMap<>();

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
        connections.values()
                .stream()
                .flatMap(it -> it.values().stream())
                .forEach(it -> {
                    try {
                        it.close();
                    } catch (Throwable ignore) {
                    }
                });
    }

    @Override
    public Connection create(Object o, Map<Object, Object> metadata) {
        ConnectionFactory factory = getConnectionFactory(o, metadata);
        if (factory == null) {
            throw new ConnectionLoadBalanceException("No MessageFactory available with " + o);
        }
        Connection connection = factory.create(o, metadata, this);
        if (connection == null) {
            throw new ConnectionLoadBalanceException("Message can not be created with " + o);
        }
        return connection;
    }

    @Override
    public Connection open(Object o, Map<Object, Object> metadata) {
        Connection connection = create(o, metadata);
        open(connection);
        return connection;
    }

    @Override
    public void open(Connection connection) {
        String type = connection.getType();
        if (type == null) {
            throw new NoConnectionTypeException(connection);
        }
        putConnection(connection, type);
        publish(new ConnectionOpenEvent(connection));
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
    public void close(Object id, String type, Object reason) {
        Connection connection = getConnections0(type).remove(id);
        if (connection == null) {
            publish(new UnknownCloseEvent(id, type, reason, this));
            return;
        }
        close(connection, reason);
    }

    @Override
    public void close(@NonNull Connection connection, Object reason) {
        publish(new ConnectionCloseEvent(connection, reason));
    }

    @Override
    public void message(Object id, String type, byte[] message) {
        Connection connection = getConnection(id, type);
        if (connection == null) {
            publish(new UnknownMessageEvent(id, type, message, this));
        } else {
            message(connection, message);
        }
    }

    @Override
    public void message(@NonNull Connection connection, byte[] message) {
        String type = connection.getType();
        if (type == null) {
            throw new NoConnectionTypeException(connection);
        }
        MessageDecoder decoder = connection.getMessageDecoder();
        Message decode = decoder.decode(message);
        switch (type) {
            case Connection.Type.CLIENT:
                publish(new MessageReceiveEvent(connection, decode));
                break;
            case Connection.Type.SUBSCRIBER:
                //转发
                send(decode);
                break;
            case Connection.Type.OBSERVABLE:
                //TODO
                //publish(new ProxyMessageReceiveEvent(connection, decode));
                //反向连接
                subscribe(decode.getPayload(), false);
                break;
        }
    }

    @Override
    public void subscribe() {
        List<ConnectionServer> servers = connectionServerProvider.getConnectionServers();
        for (ConnectionServer server : servers) {
            subscribe(server, true);
        }
    }

    @Override
    public void subscribe(ConnectionServer server, boolean reply) {
        if (containsSubscriberConnection(server)) {
            //已经存在对应的服务连接
            return;
        }
        try {
            connectionSubscriber.subscribe(server, this, connection -> {
                open(connection);
                if (reply) {
                    connection.send(createMessage(connectionServerProvider.getClient()));
                }
            });
        } catch (Throwable e) {
            publish(new ConnectionSubscribeErrorEvent(server));
        }
    }

    public boolean containsSubscriberConnection(ConnectionServer server) {
        if (server == null) {
            return false;
        }
        Collection<Connection> subscriberConnections =
                getConnections0(Connection.Type.SUBSCRIBER).values();
        for (Connection connection : subscriberConnections) {
            ConnectionServer exist = (ConnectionServer) connection.getMetadata()
                    .get(ConnectionServer.class);
            if (server.getInstanceId().equals(exist.getInstanceId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void error(Object id, String type, Throwable e) {
        Connection connection = getConnection(id, type);
        if (connection == null) {
            publish(new UnknownErrorEvent(id, type, e, this));
        } else {
            error(connection, e);
        }
    }

    @Override
    public void error(@NonNull Connection connection, Throwable e) {
        publish(new ConnectionErrorEvent(connection, e));
    }

    @Override
    public Connection getConnection(Object id, String type) {
        return getConnections0(type).get(id);
    }

    @Override
    public Map<Object, Connection> getConnections(String type) {
        return Collections.unmodifiableMap(getConnections0(type));
    }

    private void putConnection(Connection connection, String type) {
        connections.computeIfAbsent(type, k -> new ConcurrentHashMap<>())
                .put(connection.getId(), connection);
    }

    private Map<Object, Connection> getConnections0(String type) {
        return connections.getOrDefault(type, Collections.emptyMap());
    }

    @Override
    public void send(Object msg) {
        Message message = createMessage(msg);
        ConnectionSelector selector = getConnectionSelector(message);
        Connection connection;
        List<Connection> clients = new ArrayList<>(getConnections0(Connection.Type.CLIENT).values());
        List<Connection> observables = new ArrayList<>(getConnections0(Connection.Type.OBSERVABLE).values());
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
            newForward = forward + " " + instanceId;
        }
        message.getHeaders().put(Message.FORWARD, newForward);
        connection.send(message);
        publish(new MessageSendEvent(connection, message));
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

    @Override
    public void move(Object id, String fromType, String toType, Consumer<Connection> consumer) {
        Connection connection = getConnections0(fromType).remove(id);
        if (connection == null) {
            return;
        }
        consumer.accept(connection);
        if (connection.getType().equals(toType)) {
            putConnection(connection, toType);
        } else {
            throw new IllegalStateException("Change the type of connection or use #redefineType instead");
        }
    }

    @Override
    public void redefineType(@NonNull Connection connection, @NonNull String type, Connection.Redefiner redefiner) {
        connection.redefineType(type, redefiner);
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

        public T connectionSubscriber(ConnectionSubscriber subscriber) {
            this.connectionSubscriber = subscriber;
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

            messageFactories.add(new ObjectMessageFactory());

            if (eventPublisher == null) {
                eventPublisher = new DefaultConnectionEventPublisher();
            }
        }
    }
}
