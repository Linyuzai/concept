package com.github.linyuzai.event.kafka.exchange;

import com.github.linyuzai.event.core.exchange.EngineExchange;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;

public class KafkaEngineExchange extends EngineExchange {

    public KafkaEngineExchange() {
        super(KafkaEventEngine.NAME);
    }
}
