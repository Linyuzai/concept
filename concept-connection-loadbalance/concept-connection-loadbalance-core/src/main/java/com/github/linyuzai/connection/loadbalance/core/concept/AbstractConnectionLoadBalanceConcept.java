package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutor;
import com.github.linyuzai.connection.loadbalance.core.executor.ScheduledExecutorFactory;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecodeErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategyAdapter;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import com.github.linyuzai.connection.loadbalance.core.scope.ScopedFactory;
import com.github.linyuzai.connection.loadbalance.core.select.AllSelector;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.FilterConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.FilterConnectionSelectorChain;
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
 * {@link ConnectionLoadBalanceConcept} 抽象类
 */
@Getter
@Setter
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

    protected final Map<String, MessageEncoder> messageEncoderMap = new ConcurrentHashMap<>();

    protected final Map<String, MessageDecoder> messageDecoderMap = new ConcurrentHashMap<>();

    protected final Map<String, MessageRetryStrategy> messageRetryStrategyMap = new ConcurrentHashMap<>();

    /**
     * 连接仓库
     */
    protected ConnectionRepository connectionRepository;

    /**
     * 服务实例提供者
     */
    protected ConnectionServerManager connectionServerManager;

    /**
     * 连接订阅者
     */
    protected ConnectionSubscriber connectionSubscriber;

    /**
     * 连接工厂
     */
    protected List<ConnectionFactory> connectionFactories;

    /**
     * 连接选择器
     */
    protected List<ConnectionSelector> connectionSelectors;

    /**
     * 消息工厂
     */
    protected List<MessageFactory> messageFactories;

    /**
     * 消息编解码适配器
     */
    protected MessageCodecAdapter messageCodecAdapter;

    /**
     * 消息重试策略适配器
     */
    protected MessageRetryStrategyAdapter messageRetryStrategyAdapter;

    /**
     * 消息幂等校验器
     */
    protected MessageIdempotentVerifier messageIdempotentVerifier;

    /**
     * 定时执行器
     */
    protected ScheduledExecutor scheduledExecutor;

    /**
     * 事件发布者
     */
    protected ConnectionEventPublisher eventPublisher;

    private boolean initialized;

    private boolean destroyed;

    /**
     * 初始化
     * <p>
     * 尝试对所有服务实例发起订阅
     * <p>
     * 发布 {@link ConnectionLoadBalanceConceptInitializeEvent} 事件
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

    protected void onInitialize() {

    }

    /**
     * 销毁
     * <p>
     * 关闭所有连接
     * <p>
     * 发布 {@link ConnectionLoadBalanceConceptDestroyEvent} 事件
     */
    @Override
    public synchronized void destroy() {
        if (!destroyed) {
            destroyed = true;
            onDestroy();
            scheduledExecutor.shutdown();
            eventPublisher.publish(new ConnectionLoadBalanceConceptDestroyEvent(this));
        }
    }

    protected void onDestroy() {

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
    public Connection onEstablish(Object o, Map<Object, Object> metadata) {
        Connection connection = createConnection(o, metadata);
        onEstablish(connection);
        return connection;
    }

    /**
     * 当连接建立时调用
     * <p>
     * 设置 {@link ConnectionLoadBalanceConcept} 和设置编解码器
     * <p>
     * 将连接添加到连接仓库 {@link ConnectionRepository}
     * <p>
     * 发布 {@link ConnectionEstablishEvent} 事件
     *
     * @param connection 连接
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
        connectionRepository.add(connection);
        eventPublisher.publish(new ConnectionEstablishEvent(connection));
    }

    /**
     * 当连接关闭时调用
     * <p>
     * 当连接仓库 {@link ConnectionRepository} 中不存在对应的连接
     * <p>
     * 将会发布 {@link UnknownCloseEvent} 事件
     *
     * @param id     连接 id
     * @param type   连接类型
     * @param reason 关闭原因
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
     * 当连接关闭时调用
     * <p>
     * 发布 {@link ConnectionCloseEvent} 事件
     *
     * @param connection 连接
     * @param reason     关闭原因
     */
    @Override
    public void onClose(@NonNull Connection connection, Object reason) {
        Connection remove = connectionRepository.remove(connection);
        if (remove != null) {
            eventPublisher.publish(new ConnectionCloseEvent(remove, reason));
        }
    }

    /**
     * 当连接接收消息时调用
     * <p>
     * 当连接仓库 {@link ConnectionRepository} 中不存在对应的连接
     * <p>
     * 将会发布 {@link UnknownMessageEvent} 事件
     *
     * @param id      连接 id
     * @param type    连接类型
     * @param message 消息数据
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
     * 当连接接收消息时调用
     * <p>
     * 发布 {@link MessageReceiveEvent} 事件
     * <p>
     * 当消息解码失败时
     * <p>
     * 发布 {@link MessageDecodeErrorEvent} 事件
     *
     * @param connection 连接
     * @param message    消息数据
     */
    @Override
    public void onMessage(@NonNull Connection connection, Object message) {
        onMessage(connection, message, msg -> true);
    }

    @Override
    public void onMessage(Connection connection, Object message, Predicate<Message> predicate) {
        Message decode;
        try {
            MessageDecoder decoder = connection.getMessageDecoder();
            decode = decoder.decode(message);
        } catch (Throwable e) {
            eventPublisher.publish(new MessageDecodeErrorEvent(connection, e));
            return;
        }
        boolean test;
        try {
            test = predicate.test(decode);
        } catch (Throwable e) {
            eventPublisher.publish(new MessageReceivePredicateErrorEvent(connection, decode, e));
            return;
        }
        if (test) {
            eventPublisher.publish(new MessageReceiveEvent(connection, decode));
        }
    }

    /**
     * 当连接异常时调用
     * 当连接仓库 {@link ConnectionRepository} 中不存在对应的连接
     * <p>
     * 将会发布 {@link UnknownErrorEvent} 事件
     *
     * @param id   连接 id
     * @param type 连接类型
     * @param e    异常
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
     * 当连接异常时调用
     * <p>
     * 发布 {@link ConnectionErrorEvent} 事件
     *
     * @param connection 连接
     * @param e          异常
     */
    @Override
    public void onError(@NonNull Connection connection, Throwable e) {
        eventPublisher.publish(new ConnectionErrorEvent(connection, e));
    }

    /**
     * 创建消息
     * <p>
     * 如果已经是 {@link Message} 则直接返回
     * <p>
     * 通过适配消息工厂 {@link MessageFactory} 创建消息
     *
     * @param o 消息数据
     * @return {@link Message} 实例
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
     * 将消息包装成 {@link Message}
     * <p>
     * 适配对应的连接选择器 {@link ConnectionSelector}
     * <p>
     * 选择连接并标记消息被转发 {@link Message#FORWARD}
     * <p>
     * 发布 {@link MessagePrepareEvent} 事件
     * <p>
     * 执行发送消息
     * <p>
     * 发布 {@link MessageSendEvent} 事件
     * <p>
     * 如果连接选择器为选择任何连接则发布 {@link DeadMessageEvent} 事件
     *
     * @param msg 消息
     */
    @Override
    public void send(Object msg) {
        Message message = createMessage(msg);
        String messageId = messageIdempotentVerifier.generateMessageId(message);
        message.setId(messageId);
        ConnectionSelector selector = getConnectionSelector(message);
        Collection<Connection> connections = selector.select(message);
        if (connections == null || connections.isEmpty()) {
            eventPublisher.publish(new DeadMessageEvent(message));
            return;
        }
        eventPublisher.publish(new MessagePrepareEvent(message, connections));
        for (Connection connection : connections) {
            try {
                connection.send(message);
            } catch (Throwable e) {
                eventPublisher.publish(new MessageSendErrorEvent(connection, message, e));
            }
        }
        eventPublisher.publish(new MessageSendEvent(message, connections));
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

    /**
     * 适配消息工厂 {@link MessageFactory}
     *
     * @param msg 消息数据
     * @return 消息工厂
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
     * 适配连接选择器 {@link ConnectionSelector}
     *
     * @param message 消息
     * @return 连接选择器
     */
    public ConnectionSelector getConnectionSelector(Message message) {
        for (ConnectionSelector connectionSelector : connectionSelectors) {
            if (connectionSelector.support(message)) {
                return connectionSelector;
            }
        }
        return null;
    }

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

        protected List<ConnectionEventPublisherFactory> eventPublisherFactories = new ArrayList<>();

        protected List<ConnectionEventListener> eventListeners = new ArrayList<>();

        /**
         * 添加连接仓库工厂
         */
        public B addConnectionRepositoryFactories(Collection<? extends ConnectionRepositoryFactory> factories) {
            this.connectionRepositoryFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加服务实例管理器工厂
         */
        public B addConnectionServerManagerFactories(Collection<? extends ConnectionServerManagerFactory> factories) {
            this.connectionServerManagerFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加连接订阅者工厂
         */
        public B addConnectionSubscriberFactories(Collection<? extends ConnectionSubscriberFactory> factories) {
            this.connectionSubscriberFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加连接工厂
         */
        public B addConnectionFactories(Collection<? extends ConnectionFactory> factories) {
            this.connectionFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加连接选择器
         */
        public B addConnectionSelectors(Collection<? extends ConnectionSelector> selectors) {
            this.connectionSelectors.addAll(selectors);
            return (B) this;
        }

        /**
         * 添加消息工厂
         */
        public B addMessageFactories(Collection<? extends MessageFactory> factories) {
            this.messageFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加消息编解码适配器
         */
        public B addMessageCodecAdapters(Collection<? extends MessageCodecAdapter> adapters) {
            this.messageCodecAdapters.addAll(adapters);
            return (B) this;
        }

        public B addMessageRetryStrategyAdapters(Collection<? extends MessageRetryStrategyAdapter> adapters) {
            this.messageRetryStrategyAdapters.addAll(adapters);
            return (B) this;
        }

        /**
         * 添加消息幂等校验器工厂
         */
        public B addMessageIdempotentVerifierFactories(Collection<? extends MessageIdempotentVerifierFactory> factories) {
            this.messageIdempotentVerifierFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加线程池工厂
         */
        public B addScheduledExecutorFactories(Collection<ScheduledExecutorFactory> factories) {
            this.scheduledExecutorFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加事件发布者工厂
         */
        public B addEventPublisherFactories(Collection<? extends ConnectionEventPublisherFactory> factories) {
            this.eventPublisherFactories.addAll(factories);
            return (B) this;
        }

        /**
         * 添加事件监听器
         */
        public B addEventListener(ConnectionEventListener listener) {
            this.eventListeners.add(listener);
            return (B) this;
        }

        /**
         * 添加事件监听器
         */
        public B addEventListeners(Collection<ConnectionEventListener> listeners) {
            this.eventListeners.addAll(listeners);
            return (B) this;
        }

        public T build() {

            //添加一个全选器在最后
            connectionSelectors.add(new AllSelector()
                    .addScopes(getScope()));

            //添加一个任意对象的消息工厂
            messageFactories.add(new ObjectMessageFactory());

            messageCodecAdapters.add(new BaseMessageCodecAdapter()
                    .addScopes(getScope()));

            //添加消息转发处理器
            eventListeners.add(0, new MessageForwardHandler());

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
            ConnectionEventPublisher publisher = ConnectionEventPublisher.Delegate.delegate(concept,
                    withScopeFactory(ConnectionEventPublisher.class, eventPublisherFactories));
            publisher.register(withScope(eventListeners));
            concept.setEventPublisher(publisher);
            return concept;
        }

        protected abstract T create();

        protected abstract String getScope();

        protected <S extends Scoped> List<S> withScope(Collection<S> collection) {
            return Scoped.filter(getScope(), collection);
        }

        protected <S extends Scoped> S withScope(Class<S> type, Collection<S> collection) {
            return Scoped.filter(getScope(), type, collection);
        }

        protected <C, F extends ScopedFactory<C>> C withScopeFactory(Class<C> type, Collection<F> factories) {
            return ScopedFactory.create(getScope(), type, factories);
        }

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
                        case SLAVE1:
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
                    eventListeners.add(0, new MasterSlaveAutoSwitcher());
                    return new MasterSlaveSwitchableConnectionSubscriber(master, slave);
                }
            } else {
                throw new IllegalArgumentException("Master can only be one");
            }
        }

        protected MessageCodecAdapter withMessageCodecAdapterChain(ConnectionLoadBalanceConcept concept,
                                                                   List<MessageCodecAdapter> messageCodecAdapters) {
            Collections.reverse(messageCodecAdapters);
            return new MessageCodecAdapterChain(concept, messageCodecAdapters)
                    .addScopes(getScope());
        }

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
