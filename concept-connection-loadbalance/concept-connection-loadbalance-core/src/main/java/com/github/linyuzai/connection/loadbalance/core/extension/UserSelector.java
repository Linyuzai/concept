package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.select.FilterConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.MetadataSelector;

/**
 * 用户连接选择器
 * <p>
 * 配合 {@link UserMessage} 使用
 */
public class UserSelector extends MetadataSelector implements FilterConnectionSelector {

    public static final String KEY = "user-id";

    public UserSelector() {
        super(KEY);
    }
}
