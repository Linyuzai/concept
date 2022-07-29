package com.github.linyuzai.event.core.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class JacksonEventDecoder extends AbstractEventDecoder {

    private ObjectMapper objectMapper;

    public JacksonEventDecoder() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public Object doDecode(Object event, Type type) {
        if (event instanceof String) {
            if (type == String.class) {
                return event;
            }
            return objectMapper.readValue((String) event, new TypeReference<Object>() {
                @Override
                public Type getType() {
                    return type;
                }
            });
        }
        throw new EventDecodeException("String type required but " + event.getClass());
    }
}
