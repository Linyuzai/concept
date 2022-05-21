package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.select.MetadataSelector;

/**
 * 用户连接选择器
 * <p>
 * 配合 {@link UserIdMessage} 使用
 */
public class UserIdSelector extends MetadataSelector {

    public static final String KEY = "user-id";

    public UserIdSelector() {
        super(KEY);
    }
}
