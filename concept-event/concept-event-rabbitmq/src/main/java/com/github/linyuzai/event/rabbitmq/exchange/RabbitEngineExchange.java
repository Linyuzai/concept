package com.github.linyuzai.event.rabbitmq.exchange;

import com.github.linyuzai.event.core.exchange.EngineExchange;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;

public class RabbitEngineExchange extends EngineExchange {

    public RabbitEngineExchange() {
        super(RabbitEventEngine.NAME);
    }
}
