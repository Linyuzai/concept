package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

import java.util.Collection;

public class MasterSlaveAutoSwitcher extends AbstractScoped implements ConnectionEventListener {

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        try {
            recoverMasterIfNecessary(event, concept);
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new MasterRecoverErrorEvent(e));
        }
        try {
            switchSlaveIfNecessary(event, concept);
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new SlaveSwitchErrorEvent(e));
        }
    }

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
                connection.switchBy(switcher -> {
                    if (validateSlaveAndTimestamp(connection, timestamp)) {
                        if (switcher.switchMaster()) {
                            concept.getEventPublisher().publish(new MasterRecoverEvent(connection));
                        }
                    }
                });
            }
        }
    }

    public void switchSlaveIfNecessary(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof MessageSendErrorEvent) {
            Throwable error = ((MessageSendErrorEvent) event).getError();
            if (!isTransportError(error)) {
                return;
            }
            Message message = ((MessageSendErrorEvent) event).getMessage();
            if (message instanceof PingMessage || message instanceof PongMessage) {
                //默认情况下，消息发送失败之后开启 master 的 ping
                return;
            }
            MasterSlaveConnection connection =
                    getMasterConnection(((MessageSendErrorEvent) event).getConnection(), concept);
            if (connection == null) {
                return;
            }
            long timestamp = ((MessageSendErrorEvent) event).getTimestamp();
            if (validateMasterAndTimestamp(connection, timestamp)) {
                connection.switchBy(switcher -> {
                    if (validateMasterAndTimestamp(connection, timestamp)) {
                        if (switcher.switchSlave()) {
                            try {
                                //正常情况下，MessageTransportException不会直接抛出异常
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
        if (!Connection.Type.OBSERVABLE.equals(connection.getType())) {
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

    public boolean validateSlaveAndTimestamp(MasterSlaveConnection connection, long timestamp) {
        return timestamp >= connection.getSwitchTimestamp() &&
                connection.isSlave(connection.getCurrent());
    }

    public boolean validateMasterAndTimestamp(MasterSlaveConnection connection, long timestamp) {
        return timestamp >= connection.getSwitchTimestamp() &&
                connection.isMaster(connection.getCurrent());
    }

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
