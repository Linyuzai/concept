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
        implements ConnectionSubscriber {

    private final ConnectionSubscriber masterConnectionSubscriber;

    private final ConnectionSubscriber slaveConnectionSubscriber;

    @Override
    public void subscribe(Consumer<Connection> onSuccess,
                          Consumer<Throwable> onError,
                          Runnable onComplete,
                          ConnectionLoadBalanceConcept concept) {
        ConnectionImpl connection = new ConnectionImpl();
        masterConnectionSubscriber.subscribe(master ->
                connection.master = master, onError, onComplete, concept);

        slaveConnectionSubscriber.subscribe(slave ->
                connection.slave = slave, onError, onComplete, concept);

        onSuccess.accept(connection);
    }

    @Override
    public MasterSlave getMasterSlave() {
        return MasterSlave.MASTER;
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
        public void send(@NonNull Message message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
            getCurrent().send(message, onSuccess, onError, onComplete);
        }

        @Override
        public void close() {
            getConcept().onClose(this, null);
            master.close();
            slave.close();
        }

        @Override
        public void close(Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
            getConcept().onClose(this, null);
            master.close(onSuccess, onError, () ->
                    slave.close(onSuccess, onError, onComplete));
        }

        @Override
        public void close(Object reason) {
            getConcept().onClose(this, reason);
            master.close(reason);
            slave.close(reason);
        }

        @Override
        public void close(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
            getConcept().onClose(this, reason);
            master.close(reason, onSuccess, onError, () ->
                    slave.close(reason, onSuccess, onError, onComplete));
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
