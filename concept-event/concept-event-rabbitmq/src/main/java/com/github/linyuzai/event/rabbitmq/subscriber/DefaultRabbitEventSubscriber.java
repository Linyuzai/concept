package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;

@Getter
@AllArgsConstructor
public class DefaultRabbitEventSubscriber extends AbstractRabbitEventSubscriber {

    private final RabbitListenerEndpoint listenerEndpoint;

    @Override
    public MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint,
                                                                   EventContext context,
                                                                   MessageListener messageListener) {
        return endpoint.getListenerContainerFactory().createListenerContainer(listenerEndpoint);
    }
}
