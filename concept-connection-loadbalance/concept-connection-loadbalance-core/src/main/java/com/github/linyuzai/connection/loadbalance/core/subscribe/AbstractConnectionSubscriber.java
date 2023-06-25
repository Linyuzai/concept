package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

public abstract class AbstractConnectionSubscriber implements ConnectionSubscriber {

    public static final String DELIMITER = "@";

    public static final String PREFIX = "ConceptConnectionLB";

    @Override
    public synchronized void subscribe(ConnectionLoadBalanceConcept concept) {
        ConnectionServer local = concept.getConnectionServerManager().getLocal();
        String topic = getTopic(local);
        String from = getFrom(local);
        Connection connection = create(topic, concept);
        connection.getMessageSendInterceptors().add((message, con) -> {
            message.setFrom(from);
            return true;
        });
        concept.onEstablish(connection);
    }

    protected abstract Connection create(String topic, ConnectionLoadBalanceConcept concept);

    protected abstract String getExtension();

    protected void onMessage(Connection connection, Object message) {
        connection.getConcept().onMessage(connection, message, msg ->
                !getFrom(connection.getConcept().getConnectionServerManager().getLocal())
                        .equals(msg.getFrom()));
    }

    protected String getFrom(ConnectionServer local) {
        return local == null ? useUnknownIfNull(null) : useUnknownIfNull(local.getInstanceId());
    }

    protected String getTopic(ConnectionServer local) {
        return PREFIX + DELIMITER + getExtension() + DELIMITER +
                (local == null ? useUnknownIfNull(null) : useUnknownIfNull(local.getServiceId()));
    }

    protected String useUnknownIfNull(String s) {
        if (s == null) {
            return "Unknown";
        }
        return s;
    }
}
