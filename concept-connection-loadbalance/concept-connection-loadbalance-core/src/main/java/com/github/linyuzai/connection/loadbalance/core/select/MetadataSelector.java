package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public class MetadataSelector extends MessageHeaderSelector {

    public MetadataSelector(String name) {
        super(name);
    }

    @Override
    public boolean match(Connection connection, String header) {
        return header != null && header.equals(connection.getMetadata().get(getName()));
    }
}
