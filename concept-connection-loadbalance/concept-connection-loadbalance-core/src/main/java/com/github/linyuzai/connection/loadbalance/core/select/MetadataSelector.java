package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 基于使用消息头对连接的元数据进行匹配的选择器。
 * <p>
 * Select connections by matching message header and connection metadata.
 */
@Getter
@RequiredArgsConstructor
public class MetadataSelector extends MessageHeaderSelector {

    private final String key;

    @Override
    public String getHeaderName() {
        return key;
    }

    @Override
    public Object getMatchableValue(Connection connection, ConnectionLoadBalanceConcept concept) {
        return connection.getMetadata().get(key);
    }
}
