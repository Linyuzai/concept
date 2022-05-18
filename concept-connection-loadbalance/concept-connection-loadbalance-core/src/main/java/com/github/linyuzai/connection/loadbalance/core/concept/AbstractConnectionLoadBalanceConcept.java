package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecodeErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.select.AllSelector;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscribeHandler;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    protected final Map<String, Map<Object, Connection>> connections = new ConcurrentHashMap<>();

    protected final ConnectionServerProvider connectionServerProvider;

    protected final ConnectionSubscriber connectionSubscriber;

    protected final List<ConnectionFactory> connectionFactories;

    protected final List<ConnectionSelector> connectionSelectors;

    protected final List<MessageFactory> messageFactories;

    protected final MessageCodecAdapter messageCodecAdapter;

    protected final ConnectionEventPublisher eventPublisher;

    @Override
    public void initialize() {
        subscribe(true);
        publish(new ConnectionLoadBalanceConceptInitializeEvent(this));
    }

    @Override
    public void destroy() {
        connections.values()
                .stream()
                .flatMap(it -> it.values().stream())
                .forEach(Connection::close);
        publish(new ConnectionLoadBalanceConceptDestroyEvent(this));
    }

    @Override
    public Connection create(Object o, Map<Object, Object> metadata) {
        ConnectionFactory factory = getConnectionFactory(o, metadata);
        if (factory == null) {
            throw new ConnectionLoadBalanceException("No ConnectionFactory available with " + o);
        }
        Connection connection = factory.create(o, metadata, this);
        if (connection == null) {
            throw new ConnectionLoadBalanceException("Connection can not be created with " + o);
        }
        return connection;
    }

    @Override
    public Connection onOpen(Object o, Map<Object, Object> metadata) {
        Connection connection = create(o, metadata);
        onOpen(connection);
        return connection;
    }

    @Override
    public void onOpen(Connection connection) {
        String type = connection.getType();
        connection.setConcept(this);
        connection.setMessageEncoder(messageCodecAdapter.getMessageEncoder(type));
        connection.setMessageDecoder(messageCodecAdapter.getMessageDecoder(type));
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
    public void onClose(Object id, String type, Object reason) {
        Connection connection = getConnectionMapByType(type).get(id);
        if (connection == null) {
            publish(new UnknownCloseEvent(id, type, reason, this));
            return;
        }
        onClose(connection, reason);
    }

    @Override
    public void onClose(@NonNull Connection connection, Object reason) {
        getConnectionMapByType(connection.getType()).remove(connection.getId());
        publish(new ConnectionCloseEvent(connection, reason));
    }

    @Override
    public void onMessage(Object id, String type, Object message) {
        Connection connection = getConnection(id, type);
        if (connection == null) {
            publish(new UnknownMessageEvent(id, type, message, this));
        } else {
            onMessage(connection, message);
        }
    }

    @Override
    public void onMessage(@NonNull Connection connection, Object message) {
        Message decode;
        try {
            MessageDecoder decoder = connection.getMessageDecoder();
            decode = decoder.decode(message);
        } catch (Throwable e) {
            publish(new MessageDecodeErrorEvent(connection, e));
            return;
        }
        publish(new MessageReceiveEvent(connection, decode));
    }

    @Override
    public void subscribe(boolean sendServerMsg) {
        List<ConnectionServer> servers = connectionServerProvider.getConnectionServers();
        for (ConnectionServer server : servers) {
            subscribe(server, sendServerMsg);
        }
    }

    @Override
    public synchronized void subscribe(ConnectionServer server, boolean sendServerMsg) {
        Connection exist = getSubscriberConnection(server);
        if (exist != null) {
            if (exist.isAlive()) {
                return;
            } else {
                exist.close("NotAlive");
            }
        }
        try {
            connectionSubscriber.subscribe(server, this, connection -> {
                onOpen(connection);
                if (sendServerMsg) {
                    connection.send(createMessage(connectionServerProvider.getClient()));
                }
            });
        } catch (Throwable e) {
            publish(new ConnectionSubscribeErrorEvent(server, e));
        }
    }

    public Connection getSubscriberConnection(ConnectionServer server) {
        if (server == null) {
            return null;
        }
        Collection<Connection> subscriberConnections =
                getConnectionMapByType(Connection.Type.SUBSCRIBER).values();
        for (Connection connection : subscriberConnections) {
            ConnectionServer exist = (ConnectionServer) connection.getMetadata()
                    .get(ConnectionServer.class);
            if (server.getInstanceId().equals(exist.getInstanceId())) {
                return connection;
            }
        }
        return null;
    }

    @Override
    public void onError(Object id, String type, Throwable e) {
        Connection connection = getConnection(id, type);
        if (connection == null) {
            publish(new UnknownErrorEvent(id, type, e, this));
        } else {
            onError(connection, e);
        }
    }

    @Override
    public void onError(@NonNull Connection connection, Throwable e) {
        publish(new ConnectionErrorEvent(connection, e));
    }

    @Override
    public Connection getConnection(Object id, String type) {
        return getConnectionMapByType(type).get(id);
    }

    @Override
    public Collection<Connection> getConnections(String type) {
        return Collections.unmodifiableCollection(getConnectionMapByType(type).values());
    }

    private void putConnection(Connection connection, String type) {
        connections.computeIfAbsent(type, k -> new ConcurrentHashMap<>())
                .put(connection.getId(), connection);
    }

    private Map<Object, Connection> getConnectionMapByType(String type) {
        return connections.getOrDefault(type, Collections.emptyMap());
    }

    @Override
    public void send(Object msg) {
        Message message = createMessage(msg);
        ConnectionSelector selector = getConnectionSelector(message);
        List<Connection> clients = new ArrayList<>(getConnectionMapByType(Connection.Type.CLIENT).values());
        List<Connection> observables = new ArrayList<>(getConnectionMapByType(Connection.Type.OBSERVABLE).values());
        Connection connection = selector.select(message, clients, observables);
        if (connection == null) {
            publish(new DeadMessageEvent(message));
            return;
        }
        //添加转发标记，防止其他服务再次转发
        String instanceId = connectionServerProvider.getClient().getInstanceId();
        message.getHeaders().put(Message.FORWARD, instanceId);
        publish(new MessagePrepareEvent(connection, message));
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
        Connection connection = getConnectionMapByType(fromType).remove(id);
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

        protected MessageCodecAdapter messageCodecAdapter;

        protected List<MessageFactory> messageFactories = new ArrayList<>();

        protected ConnectionEventPublisher eventPublisher;

        protected List<ConnectionEventListener> eventListeners = new ArrayList<>();

        public T connectionServerProvider(ConnectionServerProvider provider) {
            this.connectionServerProvider = provider;
            return (T) this;
        }

        public T connectionSubscriber(ConnectionSubscriber subscriber) {
            this.connectionSubscriber = subscriber;
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

        public T messageCodecAdapter(MessageCodecAdapter adapter) {
            this.messageCodecAdapter = adapter;
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

        public T eventPublisher(ConnectionEventPublisher publisher) {
            this.eventPublisher = publisher;
            return (T) this;
        }

        public T addEventListener(ConnectionEventListener listener) {
            return addEventListeners(listener);
        }

        public T addEventListeners(ConnectionEventListener... listeners) {
            return addEventListeners(Arrays.asList(listeners));
        }

        public T addEventListeners(Collection<ConnectionEventListener> listeners) {
            this.eventListeners.addAll(listeners);
            return (T) this;
        }

        protected void preBuild() {
            if (connectionServerProvider == null) {
                throw new ConnectionLoadBalanceException("ConnectionServerProvider is null");
            }
            if (connectionSubscriber == null) {
                throw new ConnectionLoadBalanceException("ConnectionSubscriber is null");
            }
            if (messageCodecAdapter == null) {
                throw new ConnectionLoadBalanceException("MessageCodecAdapter is null");
            }

            connectionSelectors.add(new AllSelector());

            messageFactories.add(new ObjectMessageFactory());

            if (eventPublisher == null) {
                eventPublisher = new DefaultConnectionEventPublisher();
            }

            eventListeners.add(0, new ConnectionSubscribeHandler());
            eventListeners.add(0, new MessageForwardHandler());

            eventPublisher.register(eventListeners);
        }
    }
}
