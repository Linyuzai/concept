package com.github.linyuzai.event.rabbitmq.engine;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.engine.AbstractEventEngine;

/**
 * RabbitMQ 事件引擎
 */
public class RabbitEventEngine extends AbstractEventEngine {

    public static final String NAME = "rabbitmq";

    public RabbitEventEngine() {
        super(NAME);
    }

    /**
     * 获得 RabbitMQ 事件引擎
     */
    public static RabbitEventEngine get(EventConcept concept) {
        return (RabbitEventEngine) concept.getEngine(NAME);
    }
}
