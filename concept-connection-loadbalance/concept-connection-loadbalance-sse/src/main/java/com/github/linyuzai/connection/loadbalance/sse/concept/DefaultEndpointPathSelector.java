package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.extension.PathMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.PathSelector;
import com.github.linyuzai.connection.loadbalance.core.select.filter.FilterConnectionSelector;

/**
 * 默认的路径选择器。
 * 配合 {@link PathMessage} 使用。
 * <p>
 * Default path selector.
 * Work with {@link PathMessage}.
 */
public class DefaultEndpointPathSelector extends PathSelector
        implements FilterConnectionSelector {

    @Override
    public String getPath(Connection connection, ConnectionLoadBalanceConcept concept) {
        if (connection instanceof SseConnection) {
            return ((SseConnection) connection).getPath();
        }
        return null;
    }
}
