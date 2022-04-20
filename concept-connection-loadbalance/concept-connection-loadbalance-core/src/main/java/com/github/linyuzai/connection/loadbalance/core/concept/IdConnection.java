package com.github.linyuzai.connection.loadbalance.core.concept;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdConnection extends AbstractConnection {

    private final Object id;

    @Override
    public void doSend(byte[] bytes) {

    }

    @Override
    public void close() {

    }
}
