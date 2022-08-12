package com.github.linyuzai.event.kafka.engine;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.engine.AbstractEventEngine;

/**
 * Kafka 事件引擎
 */
public class KafkaEventEngine extends AbstractEventEngine {

    public static final String NAME = "kafka";

    public KafkaEventEngine() {
        super(NAME);
    }

    /**
     * 获得 Kafka 的事件引擎
     */
    public static KafkaEventEngine get(EventConcept concept) {
        return (KafkaEventEngine) concept.getEngine(NAME);
    }
}
