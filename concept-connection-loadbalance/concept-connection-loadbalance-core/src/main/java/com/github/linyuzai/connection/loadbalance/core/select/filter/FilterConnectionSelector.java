package com.github.linyuzai.connection.loadbalance.core.select.filter;

import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;

public interface FilterConnectionSelector extends ConnectionSelector {

    default boolean asFilter() {
        return true;
    }
}
