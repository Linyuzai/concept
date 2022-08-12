package com.github.linyuzai.event.rabbitmq.engine;

import com.github.linyuzai.event.core.engine.EventEngineFactory;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;

/**
 * RabbitMQ 事件引擎工厂
 */
public interface RabbitEventEngineFactory extends EventEngineFactory<RabbitEventProperties, RabbitEventEngine> {
}
