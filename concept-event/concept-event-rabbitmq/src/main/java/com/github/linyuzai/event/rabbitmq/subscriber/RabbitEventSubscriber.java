package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.subscriber.AbstractEventSubscriber;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

/**
 * RabbitMQ 事件订阅器
 */
public abstract class RabbitEventSubscriber extends AbstractEventSubscriber<RabbitEventEndpoint> {

}
