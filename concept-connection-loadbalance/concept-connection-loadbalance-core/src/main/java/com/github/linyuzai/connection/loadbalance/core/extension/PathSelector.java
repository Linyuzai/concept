package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.select.MessageHeaderSelector;
import lombok.Getter;

/**
 * 基于路径的连接选择器
 * <p>
 * 配合 {@link PathMessage} 使用
 */
@Getter
public abstract class PathSelector extends MessageHeaderSelector {

    public static final String KEY = "path";

    private final String prefix;

    public PathSelector() {
        this("");
    }

    public PathSelector(String prefix) {
        super(KEY);
        this.prefix = prefix;
    }

    @Override
    public boolean match(Connection connection, String header) {
        if (header == null) {
            return false;
        }
        String[] split = header.split(",");
        for (String s : split) {
            if ((prefix + s).equals(getPath(connection))) {
                return true;
            }
        }
        return false;
    }

    public abstract String getPath(Connection connection);
}
