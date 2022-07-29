package com.github.linyuzai.event.rabbitmq.endpoint;

import com.github.linyuzai.event.core.endpoint.AbstractEventEndpoint;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class RabbitEventEndpoint extends AbstractEventEndpoint {

    public RabbitEventEndpoint(@NonNull String name) {
        super(name);
    }
}
