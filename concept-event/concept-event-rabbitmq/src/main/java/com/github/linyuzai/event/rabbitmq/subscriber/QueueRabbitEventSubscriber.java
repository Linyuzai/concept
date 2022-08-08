package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import lombok.Getter;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;

@Getter
public class QueueRabbitEventSubscriber extends AbstractRabbitEventSubscriber {

    private final String[] queues;

    public QueueRabbitEventSubscriber(String... queues) {
        this.queues = queues;
    }

    @Override
    public MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint, EventContext context) {
        SimpleRabbitListenerEndpoint listenerEndpoint = new SimpleRabbitListenerEndpoint();
        listenerEndpoint.setQueueNames(queues);
        listenerEndpoint.setAdmin(endpoint.getAdmin());
        return endpoint.getListenerContainerFactory().createListenerContainer(listenerEndpoint);
    }
}
