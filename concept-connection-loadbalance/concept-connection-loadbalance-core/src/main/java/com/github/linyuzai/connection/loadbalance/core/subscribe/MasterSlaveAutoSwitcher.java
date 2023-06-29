package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEstablishEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class MasterSlaveAutoSwitcher extends AbstractScoped implements ConnectionEventListener {

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        recoverMasterIfNecessary(event, concept);
        switchSlaveIfNecessary(event, concept);
    }

    @Deprecated
    public void onMasterEstablished(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof ConnectionEstablishEvent) {
            MasterSlaveConnection connection =
                    getMasterSlaveConnection(((ConnectionEstablishEvent) event).getConnection(), concept);
            if (connection == null) {
                return;
            }
            concept.getScheduledExecutor().scheduleAtFixedRate(() ->
                            connection.send(new BinaryPingMessage()), 0, 0,
                    TimeUnit.MILLISECONDS);
        }
    }

    public void recoverMasterIfNecessary(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof MessageSendSuccessEvent &&
                ((MessageSendSuccessEvent) event).getMessage() instanceof PingMessage) {
            MasterSlaveConnection connection =
                    getMasterSlaveConnection(((MessageSendSuccessEvent) event).getConnection(), concept);
            if (connection == null) {
                return;
            }
            long timestamp = ((MessageSendSuccessEvent) event).getTimestamp();
            if (validateSlaveAndTimestamp(connection, timestamp)) {
                if (connection.getLock().tryLock()) {
                    try {
                        if (validateSlaveAndTimestamp(connection, timestamp)) {
                            connection.switchMaster();
                        }
                    } finally {
                        connection.getLock().unlock();
                    }
                }
            }
        }
    }

    public void switchSlaveIfNecessary(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof MessageSendErrorEvent) {
            Throwable error = ((MessageSendErrorEvent) event).getError();
            if (!isTransportError(error)) {
                return;
            }
            MasterSlaveConnection connection =
                    getMasterSlaveConnection(((MessageSendErrorEvent) event).getConnection(), concept);
            if (connection == null) {
                return;
            }
            long timestamp = ((MessageSendErrorEvent) event).getTimestamp();
            if (validateMasterAndTimestamp(connection, timestamp)) {
                if (connection.getLock().tryLock()) {
                    try {
                        if (validateMasterAndTimestamp(connection, timestamp)) {
                            connection.switchSlave(null);
                            Message message = ((MessageSendErrorEvent) event).getMessage();
                            try {
                                //正常情况下，MessageTransportException不会直接抛出异常
                                connection.send(message);
                            } catch (Throwable e) {
                                concept.getEventPublisher()
                                        .publish(new MessageSendErrorEvent(connection, message, e));
                            }
                        }
                    } finally {
                        connection.getLock().unlock();
                    }
                }
            }
        }
    }

    public MasterSlaveConnection getMasterSlaveConnection(Connection connection,
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
