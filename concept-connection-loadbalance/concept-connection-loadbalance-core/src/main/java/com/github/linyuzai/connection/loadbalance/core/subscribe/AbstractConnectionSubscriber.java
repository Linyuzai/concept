package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractConnectionSubscriber implements ConnectionSubscriber {

    public static final String DELIMITER = "@";

    public static final String PREFIX = "LBConnection";

    @Override
    public synchronized void subscribe(Consumer<Connection> onSuccess,
                                       Consumer<Throwable> onError,
                                       Runnable onComplete,
                                       ConnectionLoadBalanceConcept concept) {
        ConnectionServer server = getSubscriberServer();
        try {
            String topic = getTopic(server);
            //需要判断是否已经订阅对应的服务
            Connection exist = getExist(topic, concept);
            if (exist != null) {
                if (exist.isAlive()) {
                    //如果连接还存活则直接返回
                    return;
                } else {
                    //否则关闭连接
                    exist.close("NotAlive");
                }
            }
            String from = getFrom(concept);
            String name = getName(topic, from);
            Connection subscriber = new SubscriberConnection(name, Connection.Type.SUBSCRIBER);
            subscriber.getMetadata().put(ConnectionServer.class, server);
            Connection connection = create(topic, name, subscriber, concept);
            connection.getMessageSendInterceptors().add((message, con) -> {
                message.setFrom(from);
                return true;
            });
            onSuccess.accept(subscriber);
            onSuccess.accept(connection);
        } catch (Throwable e) {
            onError.accept(new ConnectionServerSubscribeException(server, e.getMessage(), e));
        } finally {
            onComplete.run();
        }
    }

    protected Connection getExist(String topic, ConnectionLoadBalanceConcept concept) {
        return concept.getConnectionRepository().get(topic, Connection.Type.OBSERVABLE);
    }

    protected abstract Connection create(String topic,
                                         String name,
                                         Connection subscriber,
                                         ConnectionLoadBalanceConcept concept);

    protected abstract ConnectionServer getSubscriberServer();

    protected void onMessageReceived(Connection connection, Object message) {
        connection.getConcept().onMessage(connection, message, msg -> {
            ConnectionLoadBalanceConcept concept = connection.getConcept();
            return !Objects.equals(getFrom(concept), msg.getFrom()) &&
                    getMessageIdempotentVerifier(concept).verify(msg);
        });
    }

    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return concept.getMessageIdempotentVerifier();
    }

    protected String getName(String topic, String from) {
        return topic + DELIMITER + from;
    }

    protected String getFrom(ConnectionLoadBalanceConcept concept) {
        ConnectionServer local = getLocal(concept);
        return local == null ? null : local.getHost() + ":" + local.getPort();
    }

    protected String getTopic(ConnectionServer server) {
        return PREFIX + DELIMITER + server.getServiceId();
    }

    protected ConnectionServer getLocal(ConnectionLoadBalanceConcept concept) {
        return concept.getConnectionServerManager().getLocal();
    }

    @Getter
    public static class SubscriberConnection extends AliveForeverConnection {

        private final Object id;

        public SubscriberConnection(String id, @NonNull String type) {
            super(type);
            this.id = id;
        }

        @Override
        public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
            onComplete.run();
        }
    }
}
