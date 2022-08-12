package com.github.linyuzai.event.kafka.exchange;

import com.github.linyuzai.event.core.exchange.EndpointExchange;

import java.util.Collection;

/**
 * Kafka 端点交换机
 * <p>
 * 定位 Kafka 引擎下的一个或多个端点
 */
public class KafkaEndpointExchange extends EndpointExchange {

    public KafkaEndpointExchange(String... endpoints) {
        super(new KafkaEngineExchange(), endpoints);
    }

    public KafkaEndpointExchange(Collection<String> endpoints) {
        super(new KafkaEngineExchange(), endpoints);
    }
}
