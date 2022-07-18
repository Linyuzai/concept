package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.engine.AbstractEventEngine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaEventEngine extends AbstractEventEngine {

    public static final String NAME = "kafka";

    public KafkaEventEngine() {
        super(NAME);
    }
}
