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

    @SneakyThrows
    @Override
    public Object decode(Object event, EventEndpoint endpoint, EventContext context) {
        if (event instanceof String) {
            Type type = context.get(Type.class);
            if (type == null || type == String.class) {
                return event;
            }
            return objectMapper.readValue((String) event, new TypeReference<Object>() {
                @Override
                public Type getType() {
                    return type;
                }
            });
        }
        throw new IllegalArgumentException("String required but " + event.getClass());
    }
}
