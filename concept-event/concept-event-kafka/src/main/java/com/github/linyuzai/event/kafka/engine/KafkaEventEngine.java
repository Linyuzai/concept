package com.github.linyuzai.event.kafka.engine;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.engine.AbstractEventEngine;

public class KafkaEventEngine extends AbstractEventEngine {

    public static final String NAME = "kafka";

    public KafkaEventEngine() {
        super(NAME);
    }

    public static KafkaEventEngine get(EventConcept concept) {
        return (KafkaEventEngine) concept.getEngine(NAME);
    }
}
