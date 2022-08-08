package com.github.linyuzai.concept.sample.event.rabbitmq;

import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpointConfigurer;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;

//@EnableRabbit
@Configuration
public class RabbitEventConfig implements RabbitEventEndpointConfigurer {

    @Override
    public void configure(RabbitEventEndpoint endpoint) {
        Queue queue = new Queue("concept-event");
        Exchange exchange = new TopicExchange("concept-event");
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("concept-event.#").noargs();

        endpoint.getAdmin().declareQueue(queue);
        endpoint.getAdmin().declareExchange(exchange);
        endpoint.getAdmin().declareBinding(binding);
    }
}
