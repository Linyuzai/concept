package com.github.linyuzai.event.core.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.Type;

/**
 * 基于 Jackson 的 json 解码器
 */
@Getter
@Setter
@AllArgsConstructor
public class JacksonEventDecoder implements EventDecoder {

    private ObjectMapper objectMapper;

    public JacksonEventDecoder() {
        this(new ObjectMapper());
    }

    /**
     * 如果未指定类型则直接返回
     * <p>
     * 如果是字符串或字节数组并且指定了对应的类型则直接返回
     * <p>
     * 否则尝试解析 json 为指定类型的对象
     */
    @SneakyThrows
    @Override
    public Object decode(Object event, EventEndpoint endpoint, EventContext context) {
        Type type = context.get(Type.class);
        if (type == null) {
            return event;
        }
        if (event instanceof String) {
            if (type == String.class) {
                return event;
            }
            return objectMapper.readValue((String) event, newTypeReference(type));
        }
        if (event instanceof byte[]) {
            if (type == byte[].class) {
                return event;
            }
            return objectMapper.readValue((byte[]) event, newTypeReference(type));
        }
        throw new IllegalArgumentException("String required but " + event.getClass());
    }

    protected TypeReference<?> newTypeReference(Type type) {
        return new TypeReference<Object>() {
            @Override
            public Type getType() {
                return type;
            }
        };
    }
}
