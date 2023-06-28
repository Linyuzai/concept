package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class MasterSlaveSwitchableConnectionSubscriber
        implements ConnectionSubscriber, ConnectionEventListener {

    private final ConnectionSubscriber masterConnectionSubscriber;

    private final ConnectionSubscriber slaveConnectionSubscriber;

    private final long period;

    @Override
    public void subscribe(Consumer<Connection> connectionConsumer, Consumer<Throwable> errorConsumer, ConnectionLoadBalanceConcept concept) {
        ConnectionImpl connection = new ConnectionImpl();
        masterConnectionSubscriber.subscribe(master ->
                connection.master = master, errorConsumer, concept);

        slaveConnectionSubscriber.subscribe(slave ->
                connection.slave = slave, errorConsumer, concept);

        connectionConsumer.accept(connection);
    }

    @Override
    public MasterSlave getMasterSlave() {
        return MasterSlave.MASTER;
    }

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof MessageSendErrorEvent) {
            Throwable error = ((MessageSendErrorEvent) event).getError();
            if (!isTransportError(error)) {
                return;
            }
            Connection connection = ((MessageSendErrorEvent) event).getConnection();
            if (connection instanceof MasterSlaveConnection) {
                MasterSlaveConnection msConnection = (MasterSlaveConnection) connection;
                long timestamp = ((MessageSendErrorEvent) event).getTimestamp();
                if (validateMasterSlaveAndTimestamp(msConnection, timestamp)) {
                    if (msConnection.getLock().tryLock()) {
                        try {
                            if (validateMasterSlaveAndTimestamp(msConnection, timestamp)) {
                                msConnection.switchSlave(null);
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
                            msConnection.getLock().unlock();
                        }
                    }
                }
            }
        }
    }

    public boolean validateMasterSlaveAndTimestamp(MasterSlaveConnection connection, long timestamp) {
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

    @Override
    public boolean support(String scope) {
        return true;
    }

    @Getter
    public static class ConnectionImpl implements MasterSlaveConnection {

        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private Connection current;

        private Connection master;

        private Connection slave;

        private long switchTimestamp;

        @Override
        public Lock getLock() {
            return lock.writeLock();
        }

        @Override
        public Connection getCurrent() {
            lock.readLock().lock();
            try {
                return current;
            } finally {
                lock.readLock().unlock();
            }
        }

        public long getSwitchTimestamp() {
            lock.readLock().lock();
            try {
                return switchTimestamp;
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isMaster(Connection connection) {
            return connection == master;
        }

        @Override
        public boolean isSlave(Connection connection) {
            return connection == slave;
        }

        @Override
        public void switchMaster() {
            lock.writeLock().lock();
            try {
                current = master;
                switchTimestamp = System.currentTimeMillis();
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void switchSlave(Object key) {
            lock.writeLock().lock();
            try {
                current = slave;
                switchTimestamp = System.currentTimeMillis();
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public Object getId() {
            return getCurrent().getId();
        }

        @Override
        public void setType(@NonNull String type) {
            getCurrent().setType(type);
        }

        @Override
        public String getType() {
            return getCurrent().getType();
        }

        @Override
        public Map<Object, Object> getMetadata() {
            return getCurrent().getMetadata();
        }

        @Override
        public void setMessageRetryStrategy(MessageRetryStrategy strategy) {
            getCurrent().setMessageRetryStrategy(strategy);
        }

        @Override
        public MessageRetryStrategy getMessageRetryStrategy() {
            return getCurrent().getMessageRetryStrategy();
        }

        @Override
        public List<MessageSendInterceptor> getMessageSendInterceptors() {
            return getCurrent().getMessageSendInterceptors();
        }

        @Override
        public void setMessageEncoder(MessageEncoder encoder) {
            getCurrent().setMessageEncoder(encoder);
        }

        @Override
        public MessageEncoder getMessageEncoder() {
            return getCurrent().getMessageEncoder();
        }

        @Override
        public void setMessageDecoder(MessageDecoder decoder) {
            getCurrent().setMessageDecoder(decoder);
        }

        @Override
        public MessageDecoder getMessageDecoder() {
            return getCurrent().getMessageDecoder();
        }

        @Override
        public void setConcept(@NonNull ConnectionLoadBalanceConcept concept) {
            getCurrent().setConcept(concept);
        }

        @Override
        public ConnectionLoadBalanceConcept getConcept() {
            return getCurrent().getConcept();
        }

        @Override
        public void send(@NonNull Message message) {
            getCurrent().send(message);
        }

        @Override
        public void send(@NonNull Message message, Runnable success, Consumer<Throwable> error) {
            getCurrent().send(message, success, error);
        }

        @Override
        public void close() {
            getConcept().onClose(this, null);
            master.close();
            slave.close();
        }

        @Override
        public void close(String reason) {
            getConcept().onClose(this, reason);
            master.close(reason);
            slave.close(reason);
        }

        @Override
        public void close(int code, String reason) {
            getConcept().onClose(this, reason);
            master.close(code, reason);
            slave.close(code, reason);
        }

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public void setAlive(boolean alive) {

        }

        @Override
        public long getLastHeartbeat() {
            return System.currentTimeMillis();
        }

        @Override
        public void setLastHeartbeat(long lastHeartbeat) {

        }
    }
}
