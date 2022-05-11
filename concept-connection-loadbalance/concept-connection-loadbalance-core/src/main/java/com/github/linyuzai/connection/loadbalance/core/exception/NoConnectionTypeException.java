package com.github.linyuzai.connection.loadbalance.core.exception;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;

@Getter
public class NoConnectionTypeException extends ConnectionLoadBalanceException {

    private final Connection connection;

    public NoConnectionTypeException(Connection connection) {
        super("Connection(" + connection.getUri() + ") type is null");
        this.connection = connection;
    }
}
