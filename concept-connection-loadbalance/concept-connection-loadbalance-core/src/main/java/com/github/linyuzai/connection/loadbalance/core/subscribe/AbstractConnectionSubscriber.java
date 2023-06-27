package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageIdempotentVerifier;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractConnectionSubscriber implements ConnectionSubscriber {

    public static final String DELIMITER = "@";

    public static final String PREFIX = "ConceptConnectionLB";

    @Override
    public synchronized void subscribe(Consumer<Connection> consumer,ConnectionLoadBalanceConcept concept) {
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
        Connection connection = create(topic, concept);
        connection.getMessageSendInterceptors().add((message, con) -> {
            message.setFrom(from);
            return true;
        });
        consumer.accept(connection);
    }

    protected Connection getExist(String topic, ConnectionLoadBalanceConcept concept) {
        return concept.getConnectionRepository().get(topic, Connection.Type.OBSERVABLE);
    }

    protected abstract Connection create(String topic, ConnectionLoadBalanceConcept concept);

    protected abstract String getExtension();

    protected void onMessage(Connection connection, Object message) {
        connection.getConcept().onMessage(connection, message, msg -> {
            ConnectionLoadBalanceConcept concept = connection.getConcept();
            return !Objects.equals(getFrom(concept), msg.getFrom()) &&
                    getMessageIdempotentVerifier(concept).verify(msg);
        });
    }

    protected MessageIdempotentVerifier getMessageIdempotentVerifier(ConnectionLoadBalanceConcept concept) {
        return concept.getMessageIdempotentVerifier();
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
