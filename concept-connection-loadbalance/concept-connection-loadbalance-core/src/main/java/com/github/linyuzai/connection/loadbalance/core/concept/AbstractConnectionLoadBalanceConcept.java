package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutor;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactory;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLogger;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLoggerFactory;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecodeErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.idempotent.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.message.idempotent.MessageIdempotentVerifierFactory;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapter;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import com.github.linyuzai.connection.loadbalance.core.scope.ScopedFactory;
import com.github.linyuzai.connection.loadbalance.core.select.AllSelector;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.filter.FilterConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.filter.FilterConnectionSelectorChain;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.*;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Concept 抽象类。
 * <p>
 * Abstract concept.
 */
@Getter
@Setter
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    protected final Map<String, MessageEncoder> messageEncoderMap = new ConcurrentHashMap<>();

    protected final Map<String, MessageDecoder> messageDecoderMap = new ConcurrentHashMap<>();

    protected final Map<String, MessageRetryStrategy> messageRetryStrategyMap = new ConcurrentHashMap<>();

    protected ConnectionRepository connectionRepository;

    protected ConnectionServerManager connectionServerManager;

    protected ConnectionSubscriber connectionSubscriber;

    protected List<ConnectionFactory> connectionFactories;

    protected List<ConnectionSelector> connectionSelectors;

    protected List<MessageFactory> messageFactories;

    protected MessageCodecAdapter messageCodecAdapter;

    protected MessageRetryStrategyAdapter messageRetryStrategyAdapter;

    protected MessageIdempotentVerifier messageIdempotentVerifier;

    protected ScheduledExecutor scheduledExecutor;

    protected ConnectionLogger logger;

    protected ConnectionEventPublisher eventPublisher;

    private boolean initialized;

    private boolean destroyed;

    /**
     * 初始化。
     * 执行订阅流程。
     * 发布 {@link ConnectionLoadBalanceConceptInitializeEvent} 事件。
     * <p>
     * Initialize.
     * Subscribe and publish {@link ConnectionLoadBalanceConceptInitializeEvent}.
     */
    @Override
    public synchronized void initialize() {
        if (!initialized) {
            initialized = true;
            onInitialize();
            connectionSubscriber.subscribe();
            eventPublisher.publish(new ConnectionLoadBalanceConceptInitializeEvent(this));
        }
    }

    /**
     * 同步执行额外的初始化操作。
     * <p>
     * Additional initialization operations in sync.
     */
    protected void onInitialize() {

    }

    /**
     * 销毁。
     * 关闭线程池。
     * 发布 {@link ConnectionLoadBalanceConceptDestroyEvent} 事件。
     * <p>
     * Destroy.
     * Shutdown executor and publish {@link ConnectionLoadBalanceConceptDestroyEvent}.
     */
    @Override
    public synchronized void destroy() {
        if (!destroyed) {
            destroyed = true;
            onDestroy();
            scheduledExecutor.shutdown();
            for (String type : connectionRepository.types()) {
                for (Connection connection : connectionRepository.select(type)) {
                    connection.close(Connection.Close.SERVER_STOP);
                }
            }
            eventPublisher.publish(new ConnectionLoadBalanceConceptDestroyEvent(this));
        }
    }

    /**
     * 同步执行额外的销毁操作。
     * <p>
     * Additional destruction operations in sync.
     */
    protected void onDestroy() {

    }

    /**
     * 创建连接。
     * 通过适配连接工厂进行创建。
     * <p>
     * Create connection by factories.
     */
    @Override
    public Connection createConnection(Object o, Map<Object, Object> metadata) {
        ConnectionFactory factory = getConnectionFactory(o, metadata);
        if (factory == null) {
            throw new ConnectionLoadBalanceException("No ConnectionFactory available with " + o);
        }
        Connection connection = factory.create(o, metadata);
        if (connection == null) {
            throw new ConnectionLoadBalanceException("Connection can not be created with " + o);
        }
        return connection;
    }

    /**
     * 适配连接工厂。
     * <p>
     * Adapt connection factory.
     */
    public ConnectionFactory getConnectionFactory(Object o, Map<Object, Object> metadata) {
        for (ConnectionFactory connectionFactory : connectionFactories) {
            if (connectionFactory.support(o, metadata)) {
                return connectionFactory;
            }
        }
        return null;
    }

    /**
     * 当连接建立时调用。
     * <p>
     * Called when connection established.
     */
    @Override
    public Connection onEstablish(Object o, Map<Object, Object> metadata) {
        Connection connection = createConnection(o, metadata);
        onEstablish(connection);
        return connection;
    }

    /**
     * 当连接建立时调用。
     * 设置 Concept 和设置编解码器。
     * 设置消息重试策略。
     * 将连接添加到连接仓库 {@link ConnectionRepository}。
     * 发布 {@link ConnectionEstablishEvent} 事件。
     * <p>
     * Called when connection established.
     * Set concept and encoder/decoder.
     * Set message retry strategy.
     * Add connection to {@link ConnectionRepository}.
     * Publish {@link ConnectionEstablishEvent}.
     */
    @Override
    public void onEstablish(Connection connection) {
        String type = connection.getType();
        connection.setConcept(this);
        MessageEncoder encoder = messageEncoderMap.computeIfAbsent(type, key ->
                messageCodecAdapter.getMessageEncoder(key, null));
        MessageDecoder decoder = messageDecoderMap.computeIfAbsent(type, key ->
                messageCodecAdapter.getMessageDecoder(key, null));
        MessageRetryStrategy retryStrategy = messageRetryStrategyMap.computeIfAbsent(type, key ->
                MessageRetryStrategy.Delegate.delegate(this,
                        messageRetryStrategyAdapter.getMessageRetryStrategy(key)));
        connection.setMessageEncoder(encoder);
        connection.setMessageDecoder(decoder);
        connection.setMessageRetryStrategy(retryStrategy);
        connection.initialize();
        connectionRepository.add(connection);
        eventPublisher.publish(new ConnectionEstablishEvent(connection));
    }

    /**
     * 当连接关闭时调用。
     * <p>
     * Called when connection closed.
     */
    @Override
    public void onClose(Object id, String type, Object reason) {
        Connection connection = connectionRepository.get(id, type);
        if (connection == null) {
            eventPublisher.publish(new UnknownCloseEvent(id, type, reason, this));
            return;
        }
        onClose(connection, reason);
    }

    /**
     * 当连接关闭时调用。
     * 将连接从 {@link ConnectionRepository} 中删除。
     * <p>
     * Called when connection closed.
     * Remove connection from {@link ConnectionRepository}.
     */
    @Override
    public void onClose(@NonNull Connection connection, Object reason) {
        Connection remove = connectionRepository.remove(connection);
        if (remove != null) {
            eventPublisher.publish(new ConnectionCloseEvent(remove, reason));
        }
    }

    /**
     * 当连接接收消息时调用。
     * <p>
     * Called when message received.
     */
    @Override
    public void onMessage(Object id, String type, Object message) {
        Connection connection = connectionRepository.get(id, type);
        if (connection == null) {
            eventPublisher.publish(new UnknownMessageEvent(id, type, message, this));
        } else {
            onMessage(connection, message);
        }
    }

    /**
     * 当连接接收消息时调用。
     * <p>
     * Called when message received.
     */
    @Override
    public void onMessage(@NonNull Connection connection, Object message) {
        onMessage(connection, message, msg -> true);
    }

    /**
     * 当连接接收消息时调用。
     * <p>
     * Called when message received.
     */
    @Override
    public void onMessage(Connection connection, Object message, Predicate<Message> predicate) {
        //解码
        //Decode message
        Message decode;
        try {
            MessageDecoder decoder = connection.getMessageDecoder();
            decode = decoder.decode(message, connection);
        } catch (Throwable e) {
            eventPublisher.publish(new MessageDecodeErrorEvent(connection, e));
            return;
        }
        //断言
        //predicate
        boolean test;
        try {
            test = predicate.test(decode);
        } catch (Throwable e) {
            eventPublisher.publish(new MessageReceivePredicateErrorEvent(connection, decode, e));
            return;
        }
        if (test) {
            //接收
            //Receive
            eventPublisher.publish(new MessageReceiveEvent(connection, decode));
        } else {
            //丢弃
            //Discard
            eventPublisher.publish(new MessageDiscardEvent(connection, decode));
        }
    }

    /**
     * 当连接异常时调用。
     * <p>
     * Called when has error.
     */
    @Override
    public void onError(Object id, String type, Throwable e) {
        Connection connection = connectionRepository.get(id, type);
        if (connection == null) {
            eventPublisher.publish(new UnknownErrorEvent(id, type, e, this));
        } else {
            onError(connection, e);
        }
    }

    /**
     * 当连接异常时调用。
     * <p>
     * Called when has error.
     */
    @Override
    public void onError(@NonNull Connection connection, Throwable e) {
        eventPublisher.publish(new ConnectionErrorEvent(connection, e));
    }

    /**
     * 创建消息。
     * <p>
     * Create message.
     */
    @Override
    public Message createMessage(Object o) {
        if (o instanceof Message) {
            return (Message) o;
        }
        MessageFactory messageFactory = getMessageFactory(o);
        if (messageFactory == null) {
            throw new ConnectionLoadBalanceException("No MessageFactory available with " + o);
        }
        Message message = messageFactory.create(o);
        if (message == null) {
            throw new ConnectionLoadBalanceException("Message can not be created with " + o);
        }
        return message;
    }

    /**
     * 创建消息。
     * 同时添加额外的消息头。
     * <p>
     * Create message with additional headers.
     */
    @Override
    public Message createMessage(Object o, Map<String, String> headers) {
        Message message = createMessage(o);
        if (headers != null) {
            message.getHeaders().putAll(headers);
        }
        return message;
    }

    /**
     * 发送消息。
     * <p>
     * Send message.
     */
    @Override
    public void send(Object msg) {
        Message message = createMessage(msg);
        ConnectionSelector selector = getConnectionSelector(message);
        doSend(message, selector);
    }

    /**
     * 发送消息。
     * 同时添加额外的消息头。
     * <p>
     * Send message with additional headers.
     */
    @Override
    public void send(Object msg, Map<String, String> headers) {
        Message message = createMessage(msg, headers);
        ConnectionSelector selector = getConnectionSelector(message);
        doSend(message, selector);
    }

    /**
     * 发送消息。
     * 同时指定连接选择器。
     * <p>
     * Send message with connection selector.
     */
    @Override
    public void send(Object msg, ConnectionSelector selector) {
        doSend(createMessage(msg), selector);
    }

    /**
     * 发送消息。
     * 同时添加额外的消息头。
     * 同时指定连接选择器。
     * <p>
     * Send message with additional headers and connection selector.
     */
    @Override
    public void send(Object msg, Map<String, String> headers, ConnectionSelector selector) {
        doSend(createMessage(msg, headers), selector);
    }

    protected void doSend(Message message, ConnectionSelector selector) {
        if (selector == null) {
            throw new IllegalArgumentException("No connection selector adapted for message: " + message);
        }
        //初始化消息
        //Init message
        initMessage(message);
        //选择连接
        //Select connections
        Collection<Connection> connections = selector.select(message);
        //设置不再转发，防止其他服务再次转发
        //Set forward flag to forbid other instances forward again
        message.setForward(false);
        if (connections == null || connections.isEmpty()) {
            //消息不会发送给任何连接
            //No connection to send message
            eventPublisher.publish(new DeadMessageEvent(message));
            return;
        }
        //消息准备
        //Message prepare
        eventPublisher.publish(new MessagePrepareEvent(message, connections));
        //遍历发送
        //Foreach send
        for (Connection connection : connections) {
            try {
                connection.send(message);
            } catch (Throwable e) {
                eventPublisher.publish(new MessageSendErrorEvent(connection, message, e));
            }
        }
        eventPublisher.publish(new MessageSendEvent(message, connections));
    }

    /**
     * 初始化消息。
     * 设置消息ID。
     * 设置消息来源。
     * <p>
     * Init message.
     * Set message id.
     * Set message from.
     */
    protected void initMessage(Message message) {
        String messageId = messageIdempotentVerifier.generateMessageId(message);
        message.setId(messageId);
        ConnectionServer local = connectionServerManager.getLocal();
        message.setFrom(ConnectionServer.url(local));
    }

    /**
     * 获得适配的消息工厂。
     * <p>
     * Get adaptive message factory .
     */
    public MessageFactory getMessageFactory(Object msg) {
        for (MessageFactory messageFactory : messageFactories) {
            if (messageFactory.support(msg)) {
                return messageFactory;
            }
        }
        return null;
    }

    /**
     * 获得适配连接选择器。
     * <p>
     * Get adaptive connection selector.
     */
    public ConnectionSelector getConnectionSelector(Message message) {
        for (ConnectionSelector connectionSelector : connectionSelectors) {
            if (connectionSelector.support(message)) {
                return connectionSelector;
            }
        }
        return null;
    }

    /**
     * Builder 抽象类。
     * <p>
     * Abstract builder for concept.
     */
    @SuppressWarnings("unchecked")
    public static abstract class AbstractBuilder<B extends AbstractBuilder<B, T>, T extends AbstractConnectionLoadBalanceConcept> {

        protected List<ConnectionRepositoryFactory> connectionRepositoryFactories = new ArrayList<>();

        protected List<ConnectionServerManagerFactory> connectionServerManagerFactories = new ArrayList<>();

        protected List<ConnectionSubscriberFactory> connectionSubscriberFactories = new ArrayList<>();

        protected List<ConnectionFactory> connectionFactories = new ArrayList<>();

        protected List<ConnectionSelector> connectionSelectors = new ArrayList<>();

        protected List<MessageFactory> messageFactories = new ArrayList<>();

        protected List<MessageCodecAdapter> messageCodecAdapters = new ArrayList<>();

        protected List<MessageRetryStrategyAdapter> messageRetryStrategyAdapters = new ArrayList<>();

        protected List<MessageIdempotentVerifierFactory> messageIdempotentVerifierFactories = new ArrayList<>();

        protected List<ScheduledExecutorFactory> scheduledExecutorFactories = new ArrayList<>();

        protected List<ConnectionLoggerFactory> loggerFactories = new ArrayList<>();

        protected List<ConnectionEventPublisherFactory> eventPublisherFactories = new ArrayList<>();

        protected List<ConnectionEventListener> eventListeners = new ArrayList<>();

        /**
         * 添加连接仓库工厂。
         * <p>
         * Add factory of connection repository.
         */
        public B addConnectionRepositoryFactories(Collection<? extends ConnectionRepositoryFactory> factories) {
            this.connectionRepositoryFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加服务实例管理器工厂。
         * <p>
         * Add factory of connection server manager.
         */
        public B addConnectionServerManagerFactories(Collection<? extends ConnectionServerManagerFactory> factories) {
            this.connectionServerManagerFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加连接订阅者工厂。
         * <p>
         * Add factory of connection subscriber.
         */
        public B addConnectionSubscriberFactories(Collection<? extends ConnectionSubscriberFactory> factories) {
            this.connectionSubscriberFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加连接工厂。
         * <p>
         * Add factory of connection.
         */
        public B addConnectionFactories(Collection<? extends ConnectionFactory> factories) {
            this.connectionFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加连接选择器。
         * <p>
         * Add connection selector.
         */
        public B addConnectionSelectors(Collection<? extends ConnectionSelector> selectors) {
            this.connectionSelectors.addAll(selectors);
            return (B) this;
        }

        /**
         * 添加消息工厂。
         * <p>
         * Add factory of message.
         */
        public B addMessageFactories(Collection<? extends MessageFactory> factories) {
            this.messageFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加消息编解码适配器。
         * <p>
         * Add adapter of message codec.
         */
        public B addMessageCodecAdapters(Collection<? extends MessageCodecAdapter> adapters) {
            this.messageCodecAdapters.addAll(adapters);
            return (B) this;
        }

        /**
         * 添加消息重试策略适配器。
         * <p>
         * Add adapter of message retry strategy.
         */
        public B addMessageRetryStrategyAdapters(Collection<? extends MessageRetryStrategyAdapter> adapters) {
            this.messageRetryStrategyAdapters.addAll(adapters);
            return (B) this;
        }

        /**
         * 添加消息幂等校验器工厂。
         * <p>
         * Add factory of message idempotent verifier.
         */
        public B addMessageIdempotentVerifierFactories(Collection<? extends MessageIdempotentVerifierFactory> factories) {
            this.messageIdempotentVerifierFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加线程池工厂。
         * <p>
         * Add factory of executor.
         */
        public B addScheduledExecutorFactories(Collection<ScheduledExecutorFactory> factories) {
            this.scheduledExecutorFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加日志工厂。
         * <p>
         * Add factory of logger.
         */
        public B addLoggerFactories(Collection<ConnectionLoggerFactory> factories) {
            this.loggerFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加事件发布者工厂。
         * <p>
         * Add factory of event publisher.
         */
        public B addEventPublisherFactories(Collection<? extends ConnectionEventPublisherFactory> factories) {
            this.eventPublisherFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加事件监听器。
         * <p>
         * Add event listener.
         */
        public B addEventListener(ConnectionEventListener listener) {
            this.eventListeners.add(listener);
            return (B) this;
        }

        /**
         * 添加事件监听器。
         * <p>
         * Add event listener.
         */
        public B addEventListeners(Collection<ConnectionEventListener> listeners) {
            this.eventListeners.addAll(listeners);
            return (B) this;
        }

        public T build() {

            //添加一个全选器在最后
            //Add an all connection selector.
            connectionSelectors.add(new AllSelector()
                    .addScopes(getScope()));

            //添加一个任意对象的消息工厂
            //Add a message factory support any object
            messageFactories.add(new ObjectMessageFactory());

            //添加一个基础消息编解码适配器
            //Add a basic message codec adapter
            messageCodecAdapters.add(new BaseMessageCodecAdapter()
                    .addScopes(getScope()));

            //添加消息转发处理器
            //Add the message forward handler
            eventListeners.add(1, new MessageForwardHandler());

            T concept = create();

            concept.setConnectionRepository(ConnectionRepository.Delegate.delegate(concept,
                    withScopeFactory(ConnectionRepository.class, connectionRepositoryFactories)));
            concept.setConnectionServerManager(ConnectionServerManager.Delegate.delegate(concept,
                    withScopeFactory(ConnectionServerManager.class, connectionServerManagerFactories)));
            concept.setConnectionSubscriber(ConnectionSubscriber.Delegate.delegate(concept,
                    withConnectionSubscriberMasterSlave(withScope(connectionSubscriberFactories))));
            concept.setConnectionFactories(ConnectionFactory.Delegate.delegate(concept,
                    withScope(connectionFactories)));
            concept.setConnectionSelectors(ConnectionSelector.Delegate.delegate(concept,
                    withConnectionSelectorFilterChain(withScope(connectionSelectors))));
            concept.setMessageFactories(MessageFactory.Delegate.delegate(concept,
                    withScope(messageFactories)));
            concept.setMessageCodecAdapter(
                    withMessageCodecAdapterChain(concept, withScope(messageCodecAdapters)));
            concept.setMessageRetryStrategyAdapter(
                    withScope(MessageRetryStrategyAdapter.class, messageRetryStrategyAdapters));
            concept.setMessageIdempotentVerifier(MessageIdempotentVerifier.Delegate.delegate(concept,
                    withScopeFactory(MessageIdempotentVerifier.class, messageIdempotentVerifierFactories)));
            concept.setScheduledExecutor(ScheduledExecutor.Delegate.delegate(concept,
                    withScopeFactory(ScheduledExecutor.class, scheduledExecutorFactories)));
            concept.setLogger(ConnectionLogger.Delegate.delegate(concept,
                    withScopeFactory(ConnectionLogger.class, loggerFactories)));
            ConnectionEventPublisher publisher = ConnectionEventPublisher.Delegate.delegate(concept,
                    withScopeFactory(ConnectionEventPublisher.class, eventPublisherFactories));
            publisher.register(withScope(eventListeners));
            concept.setEventPublisher(publisher);
            return concept;
        }

        /**
         * 创建 concept 实例。
         * <p>
         * Create an instance of concept.
         */
        protected abstract T create();

        /**
         * 获得连接域。
         * <p>
         * Get the scope.
         */
        protected abstract String getScope();

        /**
         * 根据连接域筛选。
         * <p>
         * Filter by scope.
         */
        protected <S extends Scoped> List<S> withScope(Collection<S> collection) {
            return Scoped.filter(getScope(), collection);
        }

        /**
         * 根据连接域筛选。
         * <p>
         * Filter by scope.
         */
        protected <S extends Scoped> S withScope(Class<S> type, Collection<S> collection) {
            return Scoped.filter(getScope(), type, collection);
        }

        /**
         * 根据连接域筛选并创建实例。
         * <p>
         * Filter by scope and create instance.
         */
        protected <C, F extends ScopedFactory<C>> C withScopeFactory(Class<C> type, Collection<F> factories) {
            return ScopedFactory.create(getScope(), type, factories);
        }

        /**
         * 处理主从订阅器。
         * <p>
         * Handle master-slave subscriber.
         */
        protected ConnectionSubscriber withConnectionSubscriberMasterSlave(List<ConnectionSubscriberFactory> factories) {
            List<ConnectionSubscriberFactory> unsupported = new ArrayList<>();
            List<ConnectionSubscriberFactory> masters = new ArrayList<>();
            List<ConnectionSubscriberFactory> slaves = new ArrayList<>();
            for (ConnectionSubscriberFactory factory : factories) {
                if (factory instanceof MasterSlaveConnectionSubscriberFactory) {
                    MasterSlave masterSlave =
                            ((MasterSlaveConnectionSubscriberFactory) factory).getMasterSlave();
                    switch (masterSlave) {
                        case MASTER:
                            masters.add(factory);
                            break;
                        case SLAVE:
                            slaves.add(factory);
                            break;
                    }
                } else {
                    unsupported.add(factory);
                }
            }
            if (masters.isEmpty()) {
                return withScopeFactory(ConnectionSubscriber.class, unsupported);
            } else if (masters.size() == 1) {
                ConnectionSubscriber master = withScopeFactory(ConnectionSubscriber.class, masters);
                if (slaves.isEmpty()) {
                    return new MasterFixedConnectionSubscriber(master);
                } else {
                    ConnectionSubscriber slave = withScopeFactory(ConnectionSubscriber.class, slaves);
                    eventListeners.add(1, new MasterSlaveAutoSwitcher().addScopes(getScope()));
                    return new MasterSlaveSwitchableConnectionSubscriber(master, slave);
                }
            } else {
                throw new IllegalArgumentException("Master can only be one");
            }
        }

        /**
         * 消息编解码链。
         * <p>
         * Apply chain of message codec adapter.
         */
        protected MessageCodecAdapter withMessageCodecAdapterChain(ConnectionLoadBalanceConcept concept,
                                                                   List<MessageCodecAdapter> messageCodecAdapters) {
            Collections.reverse(messageCodecAdapters);
            return new MessageCodecAdapterChain(concept, messageCodecAdapters)
                    .addScopes(getScope());
        }

        /**
         * 连接选择器过滤链。
         * <p>
         * Apply filter chain of connection selector.
         */
        protected List<ConnectionSelector> withConnectionSelectorFilterChain(List<ConnectionSelector> connectionSelectors) {
            List<ConnectionSelector> selectors = new ArrayList<>();
            List<FilterConnectionSelector> filterSelectors = new ArrayList<>();
            for (ConnectionSelector selector : connectionSelectors) {
                if (selector instanceof FilterConnectionSelector &&
                        ((FilterConnectionSelector) selector).asFilter()) {
                    filterSelectors.add((FilterConnectionSelector) selector);
                } else {
                    selectors.add(selector);
                }
            }
            selectors.add(new FilterConnectionSelectorChain(filterSelectors)
                    .addScopes(getScope()));
            return selectors;
        }
    }
}
