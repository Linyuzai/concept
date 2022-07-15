package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.engine.AbstractEventPublishEngine;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KafkaEventPublishEngine extends AbstractEventPublishEngine {

    public static final String NAME = "kafka";

    public KafkaEventPublishEngine() {
        super(NAME);
    }
}
