package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ObjectMessage extends AbstractMessage<Object> {

    public ObjectMessage(Object payload) {
        setPayload(payload);
    }
}
