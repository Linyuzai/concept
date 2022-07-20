package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.exchange.EngineExchange;

public class KafkaEngineExchange extends EngineExchange {

    public KafkaEngineExchange() {
        super(KafkaEventEngine.NAME);
    }
}
