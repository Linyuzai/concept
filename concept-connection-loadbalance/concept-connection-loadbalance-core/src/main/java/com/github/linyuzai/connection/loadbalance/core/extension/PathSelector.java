package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.select.MessageHeaderSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 路径连接选择器。
 * 配合 {@link PathMessage} 使用。
 * <p>
 * Path connection selector.
 * Used with {@link PathMessage}.
 */
@Getter
@RequiredArgsConstructor
public abstract class PathSelector extends MessageHeaderSelector {

    public static final String KEY = "_selector_path";

    private final String prefix;

    public PathSelector() {
        this("");
    }

    @Override
    public String getHeaderName() {
        return KEY;
    }

    @Override
    public String prepareMatchingValue(String value) {
        return prefix + value;
    }

    @Override
    public Object getMatchableValue(Connection connection, ConnectionLoadBalanceConcept concept) {
        return getPath(connection, concept);
    }

    public abstract String getPath(Connection connection, ConnectionLoadBalanceConcept concept);
}
