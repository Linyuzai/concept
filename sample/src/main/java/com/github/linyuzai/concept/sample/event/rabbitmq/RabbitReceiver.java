package com.github.linyuzai.concept.sample.event.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public class RabbitReceiver {

    public void receive() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
        endpoint.setQueueNames("queue");
        endpoint.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {

            }
        });
        SimpleMessageListenerContainer container = factory.createListenerContainer(endpoint);
        container.start();
    }
}
