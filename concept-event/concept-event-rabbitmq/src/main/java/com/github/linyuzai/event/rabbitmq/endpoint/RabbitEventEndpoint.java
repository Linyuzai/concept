package com.github.linyuzai.event.rabbitmq.endpoint;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.AbstractEventEndpoint;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.rabbitmq.exception.RabbitEventException;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import com.github.linyuzai.event.rabbitmq.publisher.DefaultRabbitEventPublisher;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;

@Getter
@Setter
public class RabbitEventEndpoint extends AbstractEventEndpoint {

    private RabbitEventProperties.ExtendedRabbitProperties properties;

    private ConnectionFactory connectionFactory;

    private RabbitListenerContainerFactory<? extends MessageListenerContainer> listenerContainerFactory;

    private RabbitTemplate template;

    private RabbitAdmin admin;

    public RabbitEventEndpoint(@NonNull String name, @NonNull EventEngine engine) {
        super(name, engine);
    }

    @Override
    public void defaultPublish(Object event, EventContext context) {
        new DefaultRabbitEventPublisher().publishRabbit(event, this, context);
    }

    @Override
    public Subscription defaultSubscribe(EventListener listener, EventContext context) {
        throw new RabbitEventException("EventSubscriber is null");
    }
}
