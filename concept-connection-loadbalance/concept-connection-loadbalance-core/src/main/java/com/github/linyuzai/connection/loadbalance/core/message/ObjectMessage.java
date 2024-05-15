package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.NoArgsConstructor;

/**
 * 任意对象消息。
 * <p>
 * Message with object payload.
 */
@NoArgsConstructor
public class ObjectMessage extends AbstractMessage<Object> {

    public ObjectMessage(Object payload) {
        setPayload(payload);
    }

    public ObjectMessage(Object payload, Class<?> deserializedClass) {
        setPayload(payload);
        setDeserializedClass(deserializedClass);
    }

    public void setDeserializedClass(Class<?> deserializedClass) {
        if (deserializedClass == null) {
            getHeaders().remove(Message.DESERIALIZED_CLASS);
        } else {
            getHeaders().put(Message.DESERIALIZED_CLASS, deserializedClass.getName());
        }
    }
}
