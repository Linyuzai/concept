package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.select.MetadataSelector;

public class UserIdSelector extends MetadataSelector {

    public static final String KEY = "user-id";

    public UserIdSelector() {
        super(KEY);
    }
}
