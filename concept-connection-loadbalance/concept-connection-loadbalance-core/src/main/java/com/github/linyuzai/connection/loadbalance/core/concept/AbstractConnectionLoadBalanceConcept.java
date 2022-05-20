package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecodeErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.repository.DefaultConnectionRepository;
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

/**
 * {@link ConnectionLoadBalanceConcept} 抽象类
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    /**
     * 连接仓库
     */
    protected final ConnectionRepository connectionRepository;

    /**
     * 服务实例提供者
     */
    protected final ConnectionServerProvider connectionServerProvider;

    /**
     * 连接订阅者
     */
    protected final ConnectionSubscriber connectionSubscriber;

    /**
     * 连接工厂
     */
    protected final List<ConnectionFactory> connectionFactories;

    /**
     * 连接选择器
     */
    protected final List<ConnectionSelector> connectionSelectors;

    /**
     * 消息工厂
     */
    protected final List<MessageFactory> messageFactories;

    /**
     * 消息编解码适配器
     */
    protected final MessageCodecAdapter messageCodecAdapter;

    /**
     * 事件发布者
     */
    protected final ConnectionEventPublisher eventPublisher;

    /**
     * 初始化
     * <p>
     * 尝试对所有服务实例发起订阅
     * <p>
     * 发布 {@link ConnectionLoadBalanceConceptInitializeEvent} 事件
     */
    @Override
    public void initialize() {
        subscribe(true);
        publish(new ConnectionLoadBalanceConceptInitializeEvent(this));
    }

    /**
     * 销毁
     * <p>
     * 关闭所有连接
     * <p>
     * 发布 {@link ConnectionLoadBalanceConceptDestroyEvent} 事件
     */
    @Override
    public void destroy() {
        connectionRepository.stream().forEach(connection -> connection.close("ServerStop"));
        publish(new ConnectionLoadBalanceConceptDestroyEvent(this));
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
        Collection<Connection> connections = connectionRepository.select(Connection.Type.SUBSCRIBER);
        for (Connection connection : connections) {
            ConnectionServer exist = (ConnectionServer) connection.getMetadata()
                    .get(ConnectionServer.class);
            if (server.getInstanceId().equals(exist.getInstanceId())) {
                return connection;
            }
        }
        return null;
    }

    /**
     * 创建连接
     * <p>
     * 通过适配连接工厂创建连接
     *
     * @param o        底层连接
     * @param metadata 元数据
     * @return 连接
     */
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

    /**
     * 适配连接工厂
     *
     * @param o        底层连接
     * @param metadata 元数据
     * @return 连接工厂或 null
     */
    public ConnectionFactory getConnectionFactory(Object o, Map<Object, Object> metadata) {
        for (ConnectionFactory connectionFactory : connectionFactories) {
            if (connectionFactory.support(o, metadata)) {
                return connectionFactory;
            }
        }
        return null;
    }

    @Override
    public Connection onOpen(Object o, Map<Object, Object> metadata) {
        Connection connection = create(o, metadata);
        onOpen(connection);
        return connection;
    }

    /**
     * 当连接建立时调用
     * <p>
     * 设置 {@link ConnectionLoadBalanceConcept} 和设置编解码器
     * <p>
     * 将连接添加到连接仓库 {@link ConnectionRepository}
     * <p>
     * 发布 {@link ConnectionOpenEvent} 事件
     *
     * @param connection 连接
     */
    @Override
    public void onOpen(Connection connection) {
        String type = connection.getType();
        connection.setConcept(this);
        connection.setMessageEncoder(messageCodecAdapter.getMessageEncoder(type));
        connection.setMessageDecoder(messageCodecAdapter.getMessageDecoder(type));
        connectionRepository.add(connection);
        publish(new ConnectionOpenEvent(connection));
    }

    @Override
    public void onClose(Object id, String type, Object reason) {
        Connection connection = connectionRepository.get(id, type);
        if (connection == null) {
            publish(new UnknownCloseEvent(id, type, reason, this));
            return;
        }
        onClose(connection, reason);
    }

    @Override
    public void onClose(@NonNull Connection connection, Object reason) {
        if (connectionRepository.remove(connection) == null) {
            publish(new UnknownCloseEvent(connection, reason));
        } else {
            publish(new ConnectionCloseEvent(connection, reason));
        }
    }

    @Override
    public void onMessage(Object id, String type, Object message) {
        Connection connection = connectionRepository.get(id, type);
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
    public void onError(Object id, String type, Throwable e) {
        Connection connection = connectionRepository.get(id, type);
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
    public void send(Object msg) {
        Message message = createMessage(msg);
        ConnectionSelector selector = getConnectionSelector(message);
        Connection connection = selector.select(message, connectionRepository, this);
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

    public ConnectionSelector getConnectionSelector(Message message) {
        for (ConnectionSelector connectionSelector : connectionSelectors) {
            if (connectionSelector.support(message)) {
                return connectionSelector;
            }
        }
        return null;
    }

    @Override
    public void publish(Object event) {
        eventPublisher.publish(event);
    }

    @Override
    public Connection getConnection(Object id, String type) {
        return connectionRepository.get(id, type);
    }

    @Override
    public Collection<Connection> getConnections(String type) {
        return connectionRepository.select(type);
    }


    @SuppressWarnings("unchecked")
    public static class AbstractBuilder<T extends AbstractBuilder<T>> {

        protected ConnectionRepository connectionRepository;

        protected ConnectionServerProvider connectionServerProvider;

        protected ConnectionSubscriber connectionSubscriber;

        protected List<ConnectionFactory> connectionFactories = new ArrayList<>();

        protected List<ConnectionSelector> connectionSelectors = new ArrayList<>();

        protected MessageCodecAdapter messageCodecAdapter;

        protected List<MessageFactory> messageFactories = new ArrayList<>();

        protected ConnectionEventPublisher eventPublisher;

        protected List<ConnectionEventListener> eventListeners = new ArrayList<>();

        public T connectionRepository(ConnectionRepository repository) {
            this.connectionRepository = repository;
            return (T) this;
        }

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

            if (connectionRepository == null) {
                connectionRepository = new DefaultConnectionRepository();
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
