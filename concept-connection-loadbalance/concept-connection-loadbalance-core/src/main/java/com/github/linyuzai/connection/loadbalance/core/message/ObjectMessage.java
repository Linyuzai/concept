package com.github.linyuzai.connection.loadbalance.core.message;

/**
 * 任意对象消息。
 * <p>
 * Message with object payload.
 */
public class ObjectMessage extends AbstractMessage<Object> {

    public ObjectMessage(Object payload) {
        setPayload(payload);
    }
}
