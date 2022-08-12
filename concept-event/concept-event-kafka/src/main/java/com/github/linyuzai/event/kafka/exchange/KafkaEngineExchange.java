package com.github.linyuzai.event.kafka.exchange;

import com.github.linyuzai.event.core.exchange.EngineExchange;
import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;

/**
 * Kafka 引擎交换机
 * <p>
 * 定位 Kafka 引擎下的所有端点
 */
public class KafkaEngineExchange extends EngineExchange {

    public KafkaEngineExchange() {
        super(KafkaEventEngine.NAME);
    }
}
