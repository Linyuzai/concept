package com.github.linyuzai.event.core.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * 基于 Jackson 的 json 编码器
 */
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
        if (event instanceof String) {
            return event;
        }
        return objectMapper.writeValueAsString(event);
    }
}
