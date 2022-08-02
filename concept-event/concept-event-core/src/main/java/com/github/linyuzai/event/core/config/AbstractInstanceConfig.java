package com.github.linyuzai.event.core.config;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AbstractInstanceConfig implements InstanceConfig {

    /**
     * 元数据
     */
    private Map<Object, Object> metadata;

    /**
     * 事件编码器
     */
    private EventEncoder encoder;

    /**
     * 事件解码器
     */
    private EventDecoder decoder;

    /**
     * 异常处理器
     */
    private EventErrorHandler errorHandler;

    /**
     * 事件发布器
     */
    private EventPublisher publisher;

    /**
     * 事件订阅器
     */
    private EventSubscriber subscriber;
}
