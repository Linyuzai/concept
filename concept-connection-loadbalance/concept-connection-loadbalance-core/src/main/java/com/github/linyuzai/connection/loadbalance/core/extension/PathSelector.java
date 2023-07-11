package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.select.MessageHeaderSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 基于路径的连接选择器
 * <p>
 * 配合 {@link PathMessage} 使用
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
