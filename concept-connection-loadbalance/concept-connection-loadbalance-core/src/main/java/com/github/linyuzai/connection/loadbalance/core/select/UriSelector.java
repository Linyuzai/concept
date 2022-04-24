package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public class UriSelector extends MetadataSelector {

    public UriSelector() {
        super(Connection.URI);
    }
}
