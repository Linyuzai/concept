package com.github.linyuzai.event.rabbitmq.exchange;

import com.github.linyuzai.event.core.exchange.EngineExchange;
import com.github.linyuzai.event.rabbitmq.engine.RabbitEventEngine;

/**
 * RabbitMQ 引擎交换机
 * <p>
 * 定位 RabbitMQ 引擎下的所有端点
 */
public class RabbitEngineExchange extends EngineExchange {

    public RabbitEngineExchange() {
        super(RabbitEventEngine.NAME);
    }
}
