package com.github.linyuzai.connection.loadbalance.core.select.filter;

import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;

/**
 * 作为一个过滤器。
 * <p>
 * As a filter.
 */
public interface FilterConnectionSelector extends ConnectionSelector {

    default boolean asFilter() {
        return true;
    }
}
