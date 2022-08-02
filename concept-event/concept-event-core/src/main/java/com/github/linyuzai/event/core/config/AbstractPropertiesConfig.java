package com.github.linyuzai.event.core.config;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class AbstractPropertiesConfig implements PropertiesConfig {

    private Map<Object, Object> metadata = new LinkedHashMap<>();

    private Class<? extends EventEncoder> encoder;

    private Class<? extends EventDecoder> decoder;

    private Class<? extends EventErrorHandler> errorHandler;

    private Class<? extends EventPublisher> publisher;

    private Class<? extends EventSubscriber> subscriber;
}
