package com.github.linyuzai.connection.loadbalance.core.select;

public interface FilterConnectionSelector extends ConnectionSelector {

    default boolean asFilter() {
        return true;
    }
}
