package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendErrorEvent;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendInterceptor;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class MasterSlaveConnectionSubscriberImpl implements ConnectionSubscriber, ConnectionEventListener {

    private final ConnectionSubscriber masterConnectionSubscriber;

    private final List<ConnectionSubscriber> slaveConnectionSubscribers;

    @Override
    public void subscribe(Consumer<Connection> connectionConsumer, Consumer<Throwable> errorConsumer, ConnectionLoadBalanceConcept concept) {
        ConnectionImpl connection = new ConnectionImpl();
        masterConnectionSubscriber.subscribe(master ->
                connection.master = master, errorConsumer, concept);

        for (ConnectionSubscriber slaveConnectionSubscriber : slaveConnectionSubscribers) {
            slaveConnectionSubscriber.subscribe(slave ->
                    connection.slaves.add(slave), errorConsumer, concept);
        }

        connectionConsumer.accept(connection);
    }

    @Override
    public MasterSlave getMasterSlave() {
        return MasterSlave.MASTER;
    }

    @Override
    public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        if (event instanceof MessageSendErrorEvent) {
            Connection connection = ((MessageSendErrorEvent) event).getConnection();
            if (connection instanceof MasterSlaveConnection) {
                Message message = ((MessageSendErrorEvent) event).getMessage();
                connection.send(message);
            }
        }
    }

    @Override
    public boolean support(String scope) {
        return true;
    }

    @Getter
    public static class ConnectionImpl implements MasterSlaveConnection {

        final ReadWriteLock lock = new ReentrantReadWriteLock();

        Connection current;



        int index = -1;

        Connection master;

        List<Connection> slaves = new CopyOnWriteArrayList<>();

        public Connection get() {
            lock.readLock().lock();
            try {
                return current;
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public void switchMaster() {
            lock.writeLock().lock();
            try {
                current = master;
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void switchSlave(Object key) {
            lock.writeLock().lock();
            try {
                if (index + 1 < slaves.size()) {
                    current = slaves.get(++index);
                } else {
                    getConcept().getEventPublisher()
                            .publish(new MasterSlaveSwitchFailureEvent(this));
                }
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public Object getId() {
            return get().getId();
        }

        @Override
        public void setType(@NonNull String type) {
            get().setType(type);
        }

        @Override
        public String getType() {
            return get().getType();
        }

        @Override
        public Map<Object, Object> getMetadata() {
            return get().getMetadata();
        }

        @Override
        public void setMessageRetryStrategy(MessageRetryStrategy strategy) {
            get().setMessageRetryStrategy(strategy);
        }

        @Override
        public MessageRetryStrategy getMessageRetryStrategy() {
            return get().getMessageRetryStrategy();
        }

        @Override
        public List<MessageSendInterceptor> getMessageSendInterceptors() {
            return get().getMessageSendInterceptors();
        }

        @Override
        public void setMessageEncoder(MessageEncoder encoder) {
            get().setMessageEncoder(encoder);
        }

        @Override
        public MessageEncoder getMessageEncoder() {
            return get().getMessageEncoder();
        }

        @Override
        public void setMessageDecoder(MessageDecoder decoder) {
            get().setMessageDecoder(decoder);
        }

        @Override
        public MessageDecoder getMessageDecoder() {
            return get().getMessageDecoder();
        }

        @Override
        public void setConcept(@NonNull ConnectionLoadBalanceConcept concept) {
            get().setConcept(concept);
        }

        @Override
        public ConnectionLoadBalanceConcept getConcept() {
            return get().getConcept();
        }

        @Override
        public void send(@NonNull Message message) {
            get().send(message);
        }

        @Override
        public void close() {
            get().close();
        }

        @Override
        public void close(String reason) {
            get().close(reason);
        }

        @Override
        public void close(int code, String reason) {
            get().close(code, reason);
        }

        @Override
        public boolean isAlive() {
            return get().isAlive();
        }

        @Override
        public void setAlive(boolean alive) {
            get().setAlive(alive);
        }

        @Override
        public long getLastHeartbeat() {
            return get().getLastHeartbeat();
        }

        @Override
        public void setLastHeartbeat(long lastHeartbeat) {
            get().setLastHeartbeat(lastHeartbeat);
        }
    }
}
