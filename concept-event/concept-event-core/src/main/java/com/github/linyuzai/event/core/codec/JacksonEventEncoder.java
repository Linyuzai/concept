package com.github.linyuzai.event.core.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
@AllArgsConstructor
public class JacksonEventEncoder implements EventEncoder {

    private ObjectMapper objectMapper;

    public JacksonEventEncoder() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public Object encode(Object event) {
        return objectMapper.writeValueAsString(event);
    }
}
