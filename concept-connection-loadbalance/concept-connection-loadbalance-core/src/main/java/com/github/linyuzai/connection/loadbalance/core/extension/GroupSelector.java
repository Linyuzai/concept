package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.select.FilterConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.select.MetadataSelector;

/**
 * 分组连接选择器
 * <p>
 * 配合 {@link GroupMessage} 使用
 */
public class GroupSelector extends MetadataSelector implements FilterConnectionSelector {

    public static final String KEY = "_selector_group";

    public GroupSelector() {
        super(KEY);
    }
}