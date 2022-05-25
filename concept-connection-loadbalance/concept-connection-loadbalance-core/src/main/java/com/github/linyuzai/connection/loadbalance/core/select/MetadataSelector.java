package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

/**
 * 基于使用消息头对连接的元数据进行匹配的选择器
 */
public class MetadataSelector extends MessageHeaderSelector {

    public MetadataSelector(String name) {
        super(name);
    }

    @Override
    public boolean match(Connection connection, String header) {
        if (header == null) {
            return false;
        }
        String[] split = header.split(",");
        Object o = connection.getMetadata().get(getName());
        for (String s : split) {
            if (s.equals(o)) {
                return true;
            }
        }
        return false;
    }
}
