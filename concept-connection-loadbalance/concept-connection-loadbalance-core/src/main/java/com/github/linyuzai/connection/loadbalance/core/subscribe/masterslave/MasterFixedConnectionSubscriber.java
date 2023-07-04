package com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendInterceptor;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.retry.MessageRetryStrategy;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class MasterFixedConnectionSubscriber implements ConnectionSubscriber {

    private final ConnectionSubscriber masterConnectionSubscriber;

    @Override
    public void subscribe(Consumer<Connection> onSuccess, Consumer<Throwable> onError, Runnable onComplete, ConnectionLoadBalanceConcept concept) {
        masterConnectionSubscriber.subscribe(MasterConnection::new, onError, onComplete, concept);
    }

    @Getter
    @RequiredArgsConstructor
    public static class MasterConnection implements MasterSlaveConnection,
            MasterSlaveConnection.MasterSlaveSwitcher {

        private final Connection master;

        @Override
        public Connection getSlave() {
            return null;
        }

        @Override
        public Connection getCurrent() {
            return master;
        }

        @Override
        public long getSwitchTimestamp() {
            return System.currentTimeMillis();
        }

        @Override
        public boolean isMaster(Connection connection) {
            return master == connection;
        }

        @Override
        public boolean isSlave(Connection connection) {
            return false;
        }

        @Override
        public void switchBy(Consumer<MasterSlaveSwitcher> consumer) {
            consumer.accept(this);
        }

        @Override
        public boolean switchMaster() {
            return false;
        }

        @Override
        public boolean switchSlave() {
            return false;
        }

        @Override
        public Object getId() {
            return master.getId();
        }

        @Override
        public void setType(@NonNull String type) {
            master.setType(type);
        }

        @Override
        public String getType() {
            return master.getType();
        }

        @Override
        public Map<Object, Object> getMetadata() {
            return master.getMetadata();
        }

        @Override
        public void setMessageRetryStrategy(MessageRetryStrategy strategy) {
            master.setMessageRetryStrategy(strategy);
        }

        @Override
        public MessageRetryStrategy getMessageRetryStrategy() {
            return master.getMessageRetryStrategy();
        }

        @Override
        public List<MessageSendInterceptor> getMessageSendInterceptors() {
            return master.getMessageSendInterceptors();
        }

        @Override
        public void setMessageEncoder(MessageEncoder encoder) {
            master.setMessageEncoder(encoder);
        }

        @Override
        public MessageEncoder getMessageEncoder() {
            return master.getMessageEncoder();
        }

        @Override
        public void setMessageDecoder(MessageDecoder decoder) {
            master.setMessageDecoder(decoder);
        }

        @Override
        public MessageDecoder getMessageDecoder() {
            return master.getMessageDecoder();
        }

        @Override
        public void setConcept(@NonNull ConnectionLoadBalanceConcept concept) {
            master.setConcept(concept);
        }

        @Override
        public ConnectionLoadBalanceConcept getConcept() {
            return master.getConcept();
        }

        @Override
        public void send(@NonNull Message message) {
            if (message instanceof PingMessage) {
                return;
            }
            master.send(message);
        }

        @Override
        public void send(@NonNull Message message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
            if (message instanceof PingMessage) {
                return;
            }
            master.send(message, onSuccess, onError, onComplete);
        }

        @Override
        public void close() {
            getConcept().onClose(this, null);
            master.close();
        }

        @Override
        public void close(Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
            getConcept().onClose(this, null);
            master.close(onSuccess, onError, onComplete);
        }

        @Override
        public void close(Object reason) {
            getConcept().onClose(this, reason);
            master.close(reason);
        }

        @Override
        public void close(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
            getConcept().onClose(this, reason);
            master.close(reason, onSuccess, onError, onComplete);
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
