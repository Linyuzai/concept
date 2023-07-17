package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

import java.util.Collection;

/**
 * 主从自动切换器。
 * <p>
 * Master slave auto switcher.
 */
public class MasterSlaveAutoSwitcher extends AbstractScoped implements ConnectionEventListener {

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        try {
            recoverMasterIfNecessary(event, concept);
        } catch (Throwable e) {
            concept.getEventPublisher()
                    .publish(new MasterSlaveSwitchErrorEvent(
                            new MasterSlaveSwitchException("Master recover failure", e), MasterSlave.MASTER));
        }
        try {
            switchSlaveIfNecessary(event, concept);
        } catch (Throwable e) {
            concept.getEventPublisher()
                    .publish(new MasterSlaveSwitchErrorEvent(
                            new MasterSlaveSwitchException("Slave switch failure", e), MasterSlave.SLAVE));
        }
    }

    /**
     * 如果必要，恢复 master。
     * <p>
     * If necessary, recover master.
     */
    public void recoverMasterIfNecessary(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof MessageSendSuccessEvent &&
                ((MessageSendSuccessEvent) event).getMessage() instanceof PingMessage) {
            MasterSlaveConnection connection =
                    getMasterConnection(((MessageSendSuccessEvent) event).getConnection(), concept);
            if (connection == null) {
                return;
            }
            long timestamp = ((MessageSendSuccessEvent) event).getTimestamp();
            if (validateSlaveAndTimestamp(connection, timestamp)) {
                connection.switchover(switcher -> {
                    if (validateSlaveAndTimestamp(connection, timestamp)) {
                        if (switcher.switchMaster()) {
                            concept.getLogger().info("Switch to master subscriber");
                            concept.getEventPublisher()
                                    .publish(new MasterSlaveSwitchEvent(connection, MasterSlave.MASTER));
                        }
                    }
                });
            }
        }
    }

    /**
     * 如果必要，切换 slave。
     * <p>
     * If necessary, switch slave.
     */
    public void switchSlaveIfNecessary(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof MessageSendErrorEvent) {
            Throwable error = ((MessageSendErrorEvent) event).getError();
            if (!isTransportError(error)) {
                return;
            }
            Message message = ((MessageSendErrorEvent) event).getMessage();
            if (message instanceof PingMessage || message instanceof PongMessage) {
                //默认情况下，消息发送失败之后开启 master 的 ping
                //By default, open master ping after message sending fails
                return;
            }
            MasterSlaveConnection connection =
                    getMasterConnection(((MessageSendErrorEvent) event).getConnection(), concept);
            if (connection == null) {
                return;
            }
            long timestamp = ((MessageSendErrorEvent) event).getTimestamp();
            if (validateMasterAndTimestamp(connection, timestamp)) {
                connection.switchover(switcher -> {
                    if (validateMasterAndTimestamp(connection, timestamp)) {
                        if (switcher.switchSlave()) {
                            concept.getLogger().info("Switch to slave subscriber");
                            concept.getEventPublisher()
                                    .publish(new MasterSlaveSwitchEvent(connection, MasterSlave.SLAVE));
                            try {
                                //正常情况下，MessageTransportException不会直接抛出异常
                                //Normally, MessageTransportException will not throw an exception directly
                                connection.send(message);
                            } catch (Throwable e) {
                                concept.getEventPublisher()
                                        .publish(new MessageSendErrorEvent(connection, message, e));
                            }
                        }
                    }
                });
            }
        }
    }

    public MasterSlaveConnection getMasterConnection(Connection connection,
                                                     ConnectionLoadBalanceConcept concept) {
        if (!connection.isObservableType()) {
            return null;
        }
        Collection<Connection> select = concept.getConnectionRepository()
                .select(Connection.Type.OBSERVABLE);
        for (Connection c : select) {
            if (c instanceof MasterSlaveConnection &&
                    ((MasterSlaveConnection) c).isMaster(connection)) {
                return (MasterSlaveConnection) c;
            }
        }
        return null;
    }

    /**
     * 验证 slave 和 timestamp。
     * <p>
     * Validate slave and timestamp.
     */
    public boolean validateSlaveAndTimestamp(MasterSlaveConnection connection, long timestamp) {
        return timestamp >= connection.getSwitchTimestamp() &&
                connection.isSlave(connection.getCurrent());
    }

    /**
     * 验证 master 和 timestamp。
     * <p>
     * Validate master and timestamp.
     */
    public boolean validateMasterAndTimestamp(MasterSlaveConnection connection, long timestamp) {
        return timestamp >= connection.getSwitchTimestamp() &&
                connection.isMaster(connection.getCurrent());
    }

    /**
     * 是否为传输异常。
     * <p>
     * Is it a transport exception.
     */
    public boolean isTransportError(Throwable e) {
        if (e instanceof MessageTransportException) {
            return true;
        }
        Throwable cause = e.getCause();
        if (cause != null) {
            return isTransportError(cause);
        }
        return false;
    }
}
