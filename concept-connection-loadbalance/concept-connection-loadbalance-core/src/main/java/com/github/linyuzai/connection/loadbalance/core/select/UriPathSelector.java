package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;

@Getter
public class UriPathSelector extends MessageHeaderSelector {

    public static final String KEY = "uri-path";

    private final String prefix;

    public UriPathSelector() {
        this("");
    }

    public UriPathSelector(String prefix) {
        super(KEY);
        this.prefix = prefix;
    }

    @Override
    public boolean match(Connection connection, String header) {
        return header != null && (prefix + header).equals(connection.getUri().getPath());
    }
}
