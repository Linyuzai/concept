package com.github.linyuzai.event.rabbitmq.endpoint;

import com.github.linyuzai.event.core.endpoint.AbstractEventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class RabbitEventEndpoint extends AbstractEventEndpoint {

    public RabbitEventEndpoint(@NonNull String name, @NonNull EventEngine engine) {
        super(name, engine);
    }
}
