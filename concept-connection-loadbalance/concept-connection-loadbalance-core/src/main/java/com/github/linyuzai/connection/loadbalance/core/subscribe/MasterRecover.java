package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEstablishEvent;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.heartbeat.HeartbeatSendSuccessEvent;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryPingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MasterRecover extends AbstractScoped implements ConnectionEventListener {

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        onMasterEstablished(event, concept, connection ->
                concept.getScheduledExecutor().scheduleAtFixedRate(() ->
                        connection.send(new BinaryPingMessage()), 1, 1, TimeUnit.MILLISECONDS));
        onMasterRecovered(event, concept, (heartbeat, connection) -> {
            long timestamp = heartbeat.getTimestamp();
            if (validateMasterSlaveAndTimestamp(connection, timestamp)) {
                if (connection.getLock().tryLock()) {
                    try {
                        if (validateMasterSlaveAndTimestamp(connection, timestamp)) {
                            connection.switchMaster();
                        }
                    } finally {
                        connection.getLock().unlock();
                    }
                }
            }
        });
    }

    public void onMasterEstablished(Object event, ConnectionLoadBalanceConcept concept,
                                    Consumer<MasterSlaveConnection> consumer) {
        if (event instanceof ConnectionEstablishEvent) {
            MasterSlaveConnection msConnection =
                    getMasterSlaveConnection(((ConnectionEstablishEvent) event).getConnection(), concept);
            if (msConnection != null) {
                consumer.accept(msConnection);
            }
        }
    }

    public void onMasterRecovered(Object event, ConnectionLoadBalanceConcept concept,
                                  BiConsumer<HeartbeatSendSuccessEvent, MasterSlaveConnection> consumer) {
        if (event instanceof HeartbeatSendSuccessEvent &&
                ((HeartbeatSendSuccessEvent) event).getMessage() instanceof PingMessage) {
            MasterSlaveConnection msConnection =
                    getMasterSlaveConnection(((HeartbeatSendSuccessEvent) event).getConnection(), concept);
            if (msConnection != null) {
                consumer.accept((HeartbeatSendSuccessEvent) event, msConnection);
            }
        }
    }

    public MasterSlaveConnection getMasterSlaveConnection(Connection connection,
                                                          ConnectionLoadBalanceConcept concept) {
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

    public boolean validateMasterSlaveAndTimestamp(MasterSlaveConnection connection, long timestamp) {
        return timestamp >= connection.getSwitchTimestamp() &&
                connection.isSlave(connection.getCurrent());
    }
}
