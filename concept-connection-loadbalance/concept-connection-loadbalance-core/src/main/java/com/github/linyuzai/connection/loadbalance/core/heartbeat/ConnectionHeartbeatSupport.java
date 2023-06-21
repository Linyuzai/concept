package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptDestroyEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptInitializeEvent;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryPingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageReceiveEvent;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * 心跳管理支持类
 */
@RequiredArgsConstructor
public abstract class ConnectionHeartbeatSupport extends AbstractScoped implements ConnectionEventListener {

    private ConnectionLoadBalanceConcept concept;

    /**
     * 连接类型
     */
    private final Collection<String> connectionTypes;

    /**
     * 心跳超时时间
     */
    private final long timeout;

    @Override
    public void onEvent(Object event) {
        if (event instanceof ConnectionLoadBalanceConceptInitializeEvent) {
            concept = ((ConnectionLoadBalanceConceptInitializeEvent) event).getConcept();
            onInitialize();
        } else if (event instanceof ConnectionLoadBalanceConceptDestroyEvent) {
            onDestroy();
        } else if (event instanceof MessageReceiveEvent) {
            Connection connection = ((MessageReceiveEvent) event).getConnection();
            Message message = ((MessageReceiveEvent) event).getMessage();
            //如果是 pong 则更新最后心跳时间
            if (isTypeMatched(connection.getType()) && isPongMessage(message)) {
                connection.setLastHeartbeat(System.currentTimeMillis());
                connection.setAlive(true);
            }
        }
    }

    /**
     * 连接类型是否匹配
     *
     * @param type 连接类型
     * @return 连接类型是否匹配
     */
    public boolean isTypeMatched(String type) {
        if (type == null) {
            return false;
        }
        for (String connectionType : connectionTypes) {
            if (type.equals(connectionType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化
     */
    public abstract void onInitialize();

    /**
     * 销毁
     */
    public abstract void onDestroy();

    /**
     * 发送 ping
     */
    public void sendPing() {
        for (String connectionType : connectionTypes) {
            Collection<Connection> connections = concept.getConnectionRepository().select(connectionType);
            Message message = createPingMessage();
            for (Connection connection : connections) {
                try {
                    connection.send(message);
                } catch (Throwable e) {
                    concept.getEventPublisher().publish(new HeartbeatSendErrorEvent(connection, e));
                }
            }
            concept.getEventPublisher().publish(new HeartbeatSendEvent(connections, connectionType));
        }
    }

    /**
     * 关闭心跳超时的连接
     */
    public void closeTimeout() {
        long now = System.currentTimeMillis();
        for (String connectionType : connectionTypes) {
            Collection<Connection> connections = concept.getConnectionRepository().select(connectionType);
            for (Connection connection : connections) {
                long lastHeartbeat = connection.getLastHeartbeat();
                if (now - lastHeartbeat > timeout) {
                    connection.setAlive(false);
                    connection.close("HeartbeatTimeout");
                    concept.getEventPublisher().publish(new HeartbeatTimeoutEvent(connection));
                }
            }
        }
    }

    /**
     * 是否是 pong
     *
     * @param message 消息
     * @return 是否是 pong
     */
    public boolean isPongMessage(Message message) {
        return message instanceof PongMessage;
    }

    /**
     * 创建 ping 消息
     *
     * @return ping 消息
     */
    public Message createPingMessage() {
        return new BinaryPingMessage();
    }
}
