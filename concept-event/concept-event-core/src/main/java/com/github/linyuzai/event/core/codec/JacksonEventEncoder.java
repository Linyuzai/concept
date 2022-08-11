package com.github.linyuzai.event.core.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
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

    /**
     * 如果是 null 则直接返回 null
     * <p>
     * 如果是字符串则直接返回
     * <p>
     * 否则尝试转换为 json 字符串
     */
    @SneakyThrows
    @Override
    public Object encode(Object event, EventEndpoint endpoint, EventContext context) {
        if (event == null) {
            return null;
        }
        if (event instanceof String) {
            return event;
        }
        return objectMapper.writeValueAsString(event);
    }
}
