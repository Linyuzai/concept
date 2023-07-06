package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
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
        LockableConnection connection = new LockableConnection();
        masterConnectionSubscriber.subscribe(master -> {
            if (master.isObservable()) {
                connection.master = master;
                onSuccess(connection, onSuccess);
            } else {
                onSuccess.accept(master);
            }
        }, onError, onComplete, concept);

        slaveConnectionSubscriber.subscribe(slave -> {
            if (slave.isObservable()) {
                connection.slave = slave;
                onSuccess(connection, onSuccess);
            } else {
                onSuccess.accept(slave);
            }
        }, onError, onComplete, concept);
    }

    private void onSuccess(LockableConnection connection, Consumer<Connection> onSuccess) {
        if (connection.master != null && connection.slave != null) {
            connection.current = connection.master;
            onSuccess.accept(connection);
        }
    }

    @Getter
    public static class LockableConnection implements MasterSlaveConnection,
            MasterSlaveConnection.MasterSlaveSwitcher {

        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private Connection master;

        private Connection slave;

        private Connection current;

        private long switchTimestamp;

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
        public boolean switchMaster() {
            current = master;
            switchTimestamp = System.currentTimeMillis();
            return true;
        }

        @Override
        public boolean switchSlave() {
            current = slave;
            switchTimestamp = System.currentTimeMillis();
            return true;
        }

        @SneakyThrows
        @Override
        public void switchover(Consumer<MasterSlaveSwitcher> consumer) {
            lock.writeLock().lockInterruptibly();
            try {
                consumer.accept(this);
            } finally {
                lock.writeLock().unlock();
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
        public Object getId() {
            return getCurrent().getId();
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
            if (message instanceof PingMessage) {
                if (isSlave(getCurrent())) {
                    slave.send(message);
                }
            } else {
                getCurrent().send(message);
            }
        }

        @Override
        public void send(@NonNull Message message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
            if (message instanceof PingMessage) {
                if (isSlave(getCurrent())) {
                    slave.send(message, onSuccess, onError, onComplete);
                }
            } else {
                getCurrent().send(message, onSuccess, onError, onComplete);
            }
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
