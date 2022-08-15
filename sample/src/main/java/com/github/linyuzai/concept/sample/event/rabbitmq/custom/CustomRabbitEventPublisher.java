package com.github.linyuzai.concept.sample.event.rabbitmq.custom;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.binding.RabbitBinding;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.publisher.AbstractRabbitEventPublisher;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

public class CustomRabbitEventPublisher extends AbstractRabbitEventPublisher {

    @Override
    public void send(Object event, RabbitEventEndpoint endpoint, EventContext context) {
        endpoint.getTemplate().convertAndSend(endpoint);
    }

    @Override
    public void binding(RabbitBinding binding) {
        binding.bind(new Queue("queue"))
                .to(new TopicExchange("topic"))
                .with("routingKey");
    }
}
