package com.github.linyuzai.event.core.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.Type;

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
    public Object decode(Object event, Type type) {
        return objectMapper.readValue((String) event, new TypeReference<Object>() {
            @Override
            public Type getType() {
                return type;
            }
        });
    }
}
