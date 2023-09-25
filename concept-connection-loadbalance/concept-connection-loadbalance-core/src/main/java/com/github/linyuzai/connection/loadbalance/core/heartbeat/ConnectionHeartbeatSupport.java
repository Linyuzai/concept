package com.github.linyuzai.connection.loadbalance.core.heartbeat;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptDestroyEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionLoadBalanceConceptInitializeEvent;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 心跳管理支持类。
 * <p>
 * To support heartbeat management.
 */
@Setter
@Getter
public abstract class ConnectionHeartbeatSupport extends AbstractScoped implements ConnectionEventListener {

    /**
     * 连接类型。
     * <p>
     * Connection's type to support.
     */
    private final Collection<String> connectionTypes = new CopyOnWriteArrayList<>();

    /**
     * 心跳超时时间。
     * <p>
     * Milliseconds of timeout.
     */
    private long timeout;

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof ConnectionLoadBalanceConceptInitializeEvent) {
            onInitialize(concept);
        } else if (event instanceof ConnectionLoadBalanceConceptDestroyEvent) {
            onDestroy(concept);
        } else if (event instanceof MessageReceiveEvent) {
            Connection connection = ((MessageReceiveEvent) event).getConnection();
            Message message = ((MessageReceiveEvent) event).getMessage();
            //如果是 pong 则更新最后心跳时间
            if (isTypeMatched(connection.getType()) && isHeartbeatReply(message)) {
                connection.setLastHeartbeat(System.currentTimeMillis());
                connection.setAlive(true);
            }
        }
    }

    /**
     * 连接类型是否匹配。
     * <p>
     * Match connection's type.
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

    public abstract void onInitialize(ConnectionLoadBalanceConcept concept);

    public abstract void onDestroy(ConnectionLoadBalanceConcept concept);

    /**
     * 发送心跳。
     * <p>
     * Send ping as heartbeat.
     */
    public void sendHeartbeat(ConnectionLoadBalanceConcept concept) {
        for (String connectionType : connectionTypes) {
            Collection<Connection> connections = concept.getConnectionRepository()
                    .select(connectionType);
            Message message = createHeartbeatMessage();
            for (Connection connection : connections) {
                if (connection.isClosed()) {
                    continue;
                }
                try {
                    connection.send(message);
                } catch (Throwable e) {
                    concept.getEventPublisher().publish(new MessageSendErrorEvent(connection, message, e));
                }
            }
            concept.getEventPublisher().publish(new MessageSendEvent(message, connections));
        }
    }

    /**
     * 关闭心跳超时的连接。
     * <p>
     * Close connections if heartbeat timeout.
     */
    public void closeTimeout(ConnectionLoadBalanceConcept concept) {
        long now = System.currentTimeMillis();
        for (String connectionType : connectionTypes) {
            Collection<Connection> connections = concept.getConnectionRepository().select(connectionType);
            for (Connection connection : connections) {
                if (connection.isClosed()) {
                    continue;
                }
                long lastHeartbeat = connection.getLastHeartbeat();
                if (timeout > 0 && now - lastHeartbeat > timeout) {
                    connection.setAlive(false);
                    connection.close(Connection.Close.HEARTBEAT_TIMEOUT);
                    concept.getEventPublisher().publish(new HeartbeatTimeoutEvent(connection));
                }
            }
        }
    }

    /**
     * 是否是心跳回复。
     * <p>
     * If is heartbeat reply.
     */
    public boolean isHeartbeatReply(Message message) {
        return message instanceof PongMessage;
    }

    /**
     * 创建心跳消息。
     * <p>
     * Create heartbeat message.
     */
    public Message createHeartbeatMessage() {
        return new BinaryPingMessage();
    }
}
