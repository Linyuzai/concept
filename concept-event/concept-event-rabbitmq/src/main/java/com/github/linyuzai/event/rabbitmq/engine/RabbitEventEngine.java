package com.github.linyuzai.event.rabbitmq.engine;

import com.github.linyuzai.event.core.engine.AbstractEventEngine;

public class RabbitEventEngine extends AbstractEventEngine {

    public static final String NAME = "rabbitmq";

    public RabbitEventEngine() {
        super(NAME);
    }
}
