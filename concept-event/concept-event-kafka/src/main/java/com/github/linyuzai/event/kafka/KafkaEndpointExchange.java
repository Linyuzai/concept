package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.exchange.EndpointExchange;

import java.util.Collection;

public class KafkaEndpointExchange extends EndpointExchange {

    public KafkaEndpointExchange(String... endpoints) {
        super(new KafkaEngineExchange(), endpoints);
    }

    public KafkaEndpointExchange(Collection<String> endpoints) {
        super(new KafkaEngineExchange(), endpoints);
    }
}
