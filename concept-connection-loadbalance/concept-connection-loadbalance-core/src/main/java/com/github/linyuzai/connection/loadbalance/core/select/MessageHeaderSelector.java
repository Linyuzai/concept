package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于消息头的选择器。
 * <p>
 * Select connections by message header.
 */
public abstract class MessageHeaderSelector extends AbstractConnectionSelector {

    @Override
    public boolean support(Message message, ConnectionLoadBalanceConcept concept) {
        return message.getHeaders().containsKey(getHeaderName());
    }

    @Override
    public Collection<Connection> doSelect(Message message,
                                           Collection<Connection> connections,
                                           ConnectionLoadBalanceConcept concept) {
        String headerValue = message.getHeaders().get(getHeaderName());
        return connections.stream()
                .filter(it -> match(it, headerValue, concept))
                .collect(Collectors.toList());
    }

    public boolean match(Connection connection,
                         String headerValue,
                         ConnectionLoadBalanceConcept concept) {
        if (headerValue == null) {
            return false;
        }
        List<String> values = parseHeaderValue(headerValue);
        Object matchableValue = getMatchableValue(connection, concept);
        for (String v : values) {
            String matchingValue = prepareMatchingValue(v);
            if (Objects.equals(matchingValue, matchableValue)) {
                return true;
            }
        }
        return false;
    }

    public List<String> parseHeaderValue(String headerValue) {
        return Arrays.asList(headerValue.split(","));
    }

    public String prepareMatchingValue(String value) {
        return value;
    }

    public abstract String getHeaderName();

    public abstract Object getMatchableValue(Connection connection,
                                             ConnectionLoadBalanceConcept concept);
}
