package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 基于使用消息头对连接的元数据进行匹配的选择器
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
    public boolean match(Connection connection, String header) {
        if (header == null) {
            return false;
        }
        String[] split = header.split(",");
        Object o = connection.getMetadata().get(key);
        for (String s : split) {
            if (s.equals(o)) {
                return true;
            }
        }
        return false;
    }
}
