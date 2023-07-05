package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

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
        try {
            String topic = getTopic(concept);
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
            Connection connection = create(topic, name, concept);
            connection.getMessageSendInterceptors().add((message, con) -> {
                message.setFrom(from);
                return true;
            });
            onSuccess.accept(connection);
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    protected Connection getExist(String topic, ConnectionLoadBalanceConcept concept) {
        return concept.getConnectionRepository().get(topic, Connection.Type.OBSERVABLE);
    }

    protected abstract Connection create(String topic, String name, ConnectionLoadBalanceConcept concept);

    protected abstract String getExtension();

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
        return local == null ? null : local.getInstanceId();
    }

    protected String getTopic(ConnectionLoadBalanceConcept concept) {
        ConnectionServer local = getLocal(concept);
        return PREFIX + DELIMITER + getExtension() + DELIMITER +
                (local == null ? useUnknownIfNull(null) : useUnknownIfNull(local.getServiceId()));
    }

    protected ConnectionServer getLocal(ConnectionLoadBalanceConcept concept) {
        return concept.getConnectionServerManager().getLocal();
    }

    protected String useUnknownIfNull(String s) {
        if (s == null) {
            return "Unknown";
        }
        return s;
    }
}
