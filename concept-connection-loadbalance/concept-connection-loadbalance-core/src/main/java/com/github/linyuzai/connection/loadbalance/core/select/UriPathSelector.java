package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

import java.util.Objects;

public class UriPathSelector extends MessageHeaderSelector {

    public static final String KEY = "uri_path";

    public UriPathSelector() {
        super(KEY);
    }

    @Override
    public boolean match(Connection connection, String header) {
        return Objects.equals(connection.getUri().getPath(), header);
    }
}
