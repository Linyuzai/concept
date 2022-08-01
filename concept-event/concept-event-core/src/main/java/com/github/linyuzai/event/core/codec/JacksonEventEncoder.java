package com.github.linyuzai.event.core.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.event.core.context.EventContext;
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
    public Object encode(Object event, EventContext context) {
        if (event instanceof String) {
            return event;
        }
        return objectMapper.writeValueAsString(event);
    }
}
