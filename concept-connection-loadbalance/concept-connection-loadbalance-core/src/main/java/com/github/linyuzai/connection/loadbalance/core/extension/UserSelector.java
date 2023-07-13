package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.select.FilterConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.MetadataSelector;

/**
 * 用户连接选择器。
 * 配合 {@link UserMessage} 使用。
 * <p>
 * User connection selector.
 * Used with {@link UserMessage}.
 */
public class UserSelector extends MetadataSelector implements FilterConnectionSelector {

    public static final String KEY = "_selector_user_id";

    public UserSelector() {
        super(KEY);
    }
}
