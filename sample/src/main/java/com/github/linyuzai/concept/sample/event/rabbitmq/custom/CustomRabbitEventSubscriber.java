package com.github.linyuzai.concept.sample.event.rabbitmq.custom;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.binding.RabbitBinding;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.subscriber.AbstractRabbitEventSubscriber;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;

public class CustomRabbitEventSubscriber extends AbstractRabbitEventSubscriber {

    @Override
    public MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint, EventContext context, MessageListener messageListener) {
        return endpoint.getListenerContainerFactory().createListenerContainer();
    }

    @Override
    public void binding(RabbitBinding binding) {
        binding.bind(new Queue("queue"))
                .to(new TopicExchange("topic"))
                .with("routingKey");
    }
}
