package com.github.linyuzai.event.core.codec;

import com.github.linyuzai.event.core.context.EventContext;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * 基于序列化的事件编码器
 */
public class SerializationEventEncoder implements EventEncoder {
    @SneakyThrows
    @Override
    public Object encode(Object event, EventContext context) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(event);
            return os.toByteArray();
        }
    }
}
