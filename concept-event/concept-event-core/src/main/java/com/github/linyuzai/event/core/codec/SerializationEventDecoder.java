package com.github.linyuzai.event.core.codec;

import com.github.linyuzai.event.core.context.EventContext;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * 基于序列化的事件解码器
 */
public class SerializationEventDecoder implements EventDecoder {
    @SneakyThrows
    @Override
    public Object decode(Object event, EventContext context) {
        if (event instanceof byte[]) {
            try (ByteArrayInputStream is = new ByteArrayInputStream((byte[]) event);
                 ObjectInputStream ois = new ObjectInputStream(is)) {
                return ois.readObject();
            }
        }
        throw new IllegalArgumentException("Byte array required but " + event.getClass());
    }
}
