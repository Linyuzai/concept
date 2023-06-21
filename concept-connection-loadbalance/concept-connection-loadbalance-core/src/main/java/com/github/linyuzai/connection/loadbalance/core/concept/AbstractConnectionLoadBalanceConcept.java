package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.event.*;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecodeErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryFactory;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepositoryImpl;
import com.github.linyuzai.connection.loadbalance.core.scope.ScopedFactory;
import com.github.linyuzai.connection.loadbalance.core.select.AllSelector;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.FilterConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.FilterConnectionSelectorChain;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManagerFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriberFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.function.Consumer;

/**
 * {@link ConnectionLoadBalanceConcept} 抽象类
 */
@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractConnectionLoadBalanceConcept implements ConnectionLoadBalanceConcept {

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
     * 事件发布者
     */
    protected ConnectionEventPublisher eventPublisher;

    /**
     * 初始化
     * <p>
     * 尝试对所有服务实例发起订阅
     * <p>
     * 发布 {@link ConnectionLoadBalanceConceptInitializeEvent} 事件
     */
    @Override
    public void initialize() {
        connectionSubscriber.subscribe(this);
        eventPublisher.publish(new ConnectionLoadBalanceConceptInitializeEvent(this));
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
        //Spring会帮忙调用close方法
        //connectionRepository.stream().forEach(connection -> connection.close("ServerStop"));
        eventPublisher.publish(new ConnectionLoadBalanceConceptDestroyEvent(this));
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
        connection.setMessageEncoder(messageCodecAdapter.getMessageEncoder(type));
        connection.setMessageDecoder(messageCodecAdapter.getMessageDecoder(type));
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
        Message decode;
        try {
            MessageDecoder decoder = connection.getMessageDecoder();
            decode = decoder.decode(message);
        } catch (Throwable e) {
            eventPublisher.publish(new MessageDecodeErrorEvent(connection, e));
            return;
        }
        eventPublisher.publish(new MessageReceiveEvent(connection, decode));
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
        ConnectionSelector selector = getConnectionSelector(message);
        Collection<Connection> connections = selector.select(message, connectionRepository, this);
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
    @RequiredArgsConstructor
    public static class AbstractBuilder<T extends AbstractBuilder<T>> {

        protected final String scope;

        protected List<ConnectionRepositoryFactory> connectionRepositoryFactories = new ArrayList<>();

        protected List<ConnectionServerManagerFactory> connectionServerManagerFactories = new ArrayList<>();

        protected List<ConnectionSubscriberFactory> connectionSubscriberFactories = new ArrayList<>();

        protected List<ConnectionFactory> connectionFactories = new ArrayList<>();

        protected List<ConnectionSelector> connectionSelectors = new ArrayList<>();

        protected List<MessageCodecAdapterFactory> messageCodecAdapterFactories = new ArrayList<>();

        protected List<MessageFactory> messageFactories = new ArrayList<>();

        protected List<ConnectionEventPublisherFactory> eventPublisherFactories = new ArrayList<>();

        protected List<ConnectionEventListener> eventListeners = new ArrayList<>();

        /**
         * 添加连接仓库工厂
         */
        public T addConnectionRepositoryFactories(Collection<? extends ConnectionRepositoryFactory> factories) {
            this.connectionRepositoryFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加服务实例管理器工厂
         */
        public T addConnectionServerManagerFactories(Collection<? extends ConnectionServerManagerFactory> factories) {
            this.connectionServerManagerFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加连接订阅者工厂
         */
        public T addConnectionSubscriberFactories(Collection<? extends ConnectionSubscriberFactory> factories) {
            this.connectionSubscriberFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加连接工厂
         */
        public T addConnectionFactories(Collection<? extends ConnectionFactory> factories) {
            this.connectionFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加连接选择器
         */
        public T addConnectionSelectors(Collection<? extends ConnectionSelector> selectors) {
            this.connectionSelectors.addAll(selectors);
            return (T) this;
        }

        /**
         * 添加消息编解码适配器工厂
         */
        public T addMessageCodecAdapterFactories(Collection<? extends MessageCodecAdapterFactory> factories) {
            this.messageCodecAdapterFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加消息工厂
         */
        public T addMessageFactories(Collection<? extends MessageFactory> factories) {
            this.messageFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加事件发布者工厂
         */
        public T addEventPublisherFactories(Collection<? extends ConnectionEventPublisherFactory> factories) {
            this.eventPublisherFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加事件监听器
         */
        public T addEventListener(ConnectionEventListener listener) {
            this.eventListeners.add(listener);
            return (T) this;
        }

        /**
         * 添加事件监听器
         */
        public T addEventListeners(Collection<ConnectionEventListener> listeners) {
            this.eventListeners.addAll(listeners);
            return (T) this;
        }

        protected void init() {

            //添加一个全选器在最后
            connectionSelectors.add(new AllSelector());

            //添加一个任意对象的消息工厂
            messageFactories.add(new ObjectMessageFactory());

            //添加消息转发处理器
            eventListeners.add(0, new MessageForwardHandler());
        }

        protected <C, F extends ScopedFactory<C>> C withScope(Class<C> type, Collection<F> factories) {
            return withScope(type, factories, null);
        }

        protected <C, F extends ScopedFactory<C>> C withScope(Class<C> type, Collection<F> factories, Consumer<C> consumer) {
            for (F factory : factories) {
                if (factory.support(scope)) {
                    C c = factory.create(scope);
                    if (consumer != null) {
                        consumer.accept(c);
                    }
                    return c;
                }
            }
            throw new ConnectionLoadBalanceException(type.getName() + " not found");
        }

        protected List<ConnectionSelector> withFilterChain() {
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
            selectors.add(new FilterConnectionSelectorChain(filterSelectors));
            return selectors;
        }
    }
}
