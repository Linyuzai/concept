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
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
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
    protected final ConnectionServerManager connectionServerManager;

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
        //Spring会帮忙调用close方法
        //connectionRepository.stream().forEach(connection -> connection.close("ServerStop"));
        publish(new ConnectionLoadBalanceConceptDestroyEvent(this));
    }

    @Override
    public void subscribe(boolean sendServerMsg) {
        List<ConnectionServer> servers = connectionServerManager.getConnectionServers();
        for (ConnectionServer server : servers) {
            subscribe(server, sendServerMsg);
        }
    }

    /**
     * 尝试订阅服务实例
     * <p>
     * 当对应的连接存在时
     * <p>
     * 如果存活 {@link Connection#isAlive()} 就不进行重复订阅
     * <p>
     * 否则关闭之前的连接并重新订阅
     * <p>
     * 加锁防止主动订阅和被动订阅冲突
     * <p>
     * 当接收到服务实例信息时会反向订阅 {@link ConnectionSubscribeHandler}
     *
     * @param server        需要订阅的服务实例
     * @param sendServerMsg 是否发送服务实例消息
     */
    @Override
    public synchronized void subscribe(ConnectionServer server, boolean sendServerMsg) {
        //需要判断是否已经订阅对应的服务
        Connection exist = getSubscriberConnection(server);
        if (exist != null) {
            if (exist.isAlive()) {
                //如果连接还存活则直接返回
                return;
            } else {
                //否则关闭连接
                exist.close("NotAlive");
            }
        }
        try {
            connectionSubscriber.subscribe(server, this, connection -> {
                onEstablish(connection);
                if (sendServerMsg) {
                    connection.send(createMessage(connectionServerManager.getLocal()));
                }
            });
        } catch (Throwable e) {
            publish(new ConnectionSubscribeErrorEvent(server, e));
        }
    }

    /**
     * 获得订阅了对应服务实例的连接
     * <p>
     * 如不存在则返回 null
     *
     * @param server 服务实例
     * @return 对应的连接或 null
     */
    public Connection getSubscriberConnection(ConnectionServer server) {
        if (server == null) {
            return null;
        }
        Collection<Connection> connections = connectionRepository.select(Connection.Type.SUBSCRIBER);
        for (Connection connection : connections) {
            ConnectionServer exist = (ConnectionServer) connection.getMetadata().get(ConnectionServer.class);
            if (connectionServerManager.isEqual(server, exist)) {
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
    public Connection onEstablish(Object o, Map<Object, Object> metadata) {
        Connection connection = create(o, metadata);
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
        publish(new ConnectionEstablishEvent(connection));
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
            publish(new UnknownCloseEvent(id, type, reason, this));
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
            publish(new ConnectionCloseEvent(remove, reason));
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
            publish(new UnknownMessageEvent(id, type, message, this));
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
            publish(new MessageDecodeErrorEvent(connection, e));
            return;
        }
        publish(new MessageReceiveEvent(connection, decode));
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
            publish(new UnknownErrorEvent(id, type, e, this));
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
        publish(new ConnectionErrorEvent(connection, e));
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
            publish(new DeadMessageEvent(message));
            return;
        }
        publish(new MessagePrepareEvent(message, connections));
        for (Connection connection : connections) {
            try {
                connection.send(message);
            } catch (Throwable e) {
                publish(new MessageSendErrorEvent(connection, message, e));
            }
        }
        publish(new MessageSendEvent(message, connections));
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
     * 创建消息
     * <p>
     * 如果已经是 {@link Message} 则直接返回
     * <p>
     * 通过适配消息工厂 {@link MessageFactory} 创建消息
     *
     * @param msg 消息数据
     * @return {@link Message} 实例
     */
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

        protected ConnectionServerManager connectionServerManager;

        protected ConnectionSubscriber connectionSubscriber;

        protected List<ConnectionFactory> connectionFactories = new ArrayList<>();

        protected List<ConnectionSelector> connectionSelectors = new ArrayList<>();

        protected MessageCodecAdapter messageCodecAdapter;

        protected List<MessageFactory> messageFactories = new ArrayList<>();

        protected ConnectionEventPublisher eventPublisher;

        protected List<ConnectionEventListener> eventListeners = new ArrayList<>();

        /**
         * 设置连接仓库
         *
         * @param repository 连接仓库
         * @return Builder
         */
        public T connectionRepository(ConnectionRepository repository) {
            this.connectionRepository = repository;
            return (T) this;
        }

        /**
         * 设置服务实例提供者
         *
         * @param manager 服务实例提供者
         * @return Builder
         */
        public T connectionServerManager(ConnectionServerManager manager) {
            this.connectionServerManager = manager;
            return (T) this;
        }

        /**
         * 设置连接订阅者
         *
         * @param subscriber 连接订阅者
         * @return Builder
         */
        public T connectionSubscriber(ConnectionSubscriber subscriber) {
            this.connectionSubscriber = subscriber;
            return (T) this;
        }

        /**
         * 添加连接工厂
         *
         * @param factory 连接工厂
         * @return Builder
         */
        public T addConnectionFactory(ConnectionFactory factory) {
            return addConnectionFactories(factory);
        }

        /**
         * 添加连接工厂
         *
         * @param factories 连接工厂
         * @return Builder
         */
        public T addConnectionFactories(ConnectionFactory... factories) {
            return addConnectionFactories(Arrays.asList(factories));
        }

        /**
         * 添加连接工厂
         *
         * @param factories 连接工厂
         * @return Builder
         */
        public T addConnectionFactories(Collection<? extends ConnectionFactory> factories) {
            this.connectionFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 添加连接选择器
         *
         * @param selector 连接选择器
         * @return Builder
         */
        public T addConnectionSelector(ConnectionSelector selector) {
            return addConnectionSelectors(selector);
        }

        /**
         * 添加连接选择器
         *
         * @param selectors 连接选择器
         * @return Builder
         */
        public T addConnectionSelectors(ConnectionSelector... selectors) {
            return addConnectionSelectors(Arrays.asList(selectors));
        }

        /**
         * 添加连接选择器
         *
         * @param selectors 连接选择器
         * @return Builder
         */
        public T addConnectionSelectors(Collection<? extends ConnectionSelector> selectors) {
            this.connectionSelectors.addAll(selectors);
            return (T) this;
        }

        /**
         * 设置消息编解码适配器
         *
         * @param adapter 消息编解码适配器
         * @return Builder
         */
        public T messageCodecAdapter(MessageCodecAdapter adapter) {
            this.messageCodecAdapter = adapter;
            return (T) this;
        }

        /**
         * 添加消息工厂
         *
         * @param factory 消息工厂
         * @return Builder
         */
        public T addMessageFactory(MessageFactory factory) {
            return addMessageFactories(factory);
        }

        /**
         * 添加消息工厂
         *
         * @param factories 消息工厂
         * @return Builder
         */
        public T addMessageFactories(MessageFactory... factories) {
            return addMessageFactories(Arrays.asList(factories));
        }

        /**
         * 添加消息工厂
         *
         * @param factories 消息工厂
         * @return Builder
         */
        public T addMessageFactories(Collection<? extends MessageFactory> factories) {
            this.messageFactories.addAll(factories);
            return (T) this;
        }

        /**
         * 设置事件发布者
         *
         * @param publisher 事件发布者
         * @return Builder
         */
        public T eventPublisher(ConnectionEventPublisher publisher) {
            this.eventPublisher = publisher;
            return (T) this;
        }

        /**
         * 添加事件监听器
         *
         * @param listener 事件监听器
         * @return Builder
         */
        public T addEventListener(ConnectionEventListener listener) {
            return addEventListeners(listener);
        }

        /**
         * 添加事件监听器
         *
         * @param listeners 事件监听器
         * @return Builder
         */
        public T addEventListeners(ConnectionEventListener... listeners) {
            return addEventListeners(Arrays.asList(listeners));
        }

        /**
         * 添加事件监听器
         *
         * @param listeners 事件监听器
         * @return Builder
         */
        public T addEventListeners(Collection<ConnectionEventListener> listeners) {
            this.eventListeners.addAll(listeners);
            return (T) this;
        }

        protected void preBuild() {
            if (connectionServerManager == null) {
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

            //添加一个全选器在最后
            connectionSelectors.add(new AllSelector());

            //添加一个任意对象的消息工厂
            messageFactories.add(new ObjectMessageFactory());

            if (eventPublisher == null) {
                eventPublisher = new DefaultConnectionEventPublisher();
            }

            //添加连接反向订阅处理器
            eventListeners.add(0, new ConnectionSubscribeHandler());
            //添加消息转发处理器
            eventListeners.add(0, new MessageForwardHandler());

            eventPublisher.register(eventListeners);
        }
    }
}
