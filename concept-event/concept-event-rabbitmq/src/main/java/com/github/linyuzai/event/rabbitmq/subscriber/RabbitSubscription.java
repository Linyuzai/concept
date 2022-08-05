package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.subscriber.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;

@Getter
@AllArgsConstructor
public class RabbitSubscription implements Subscription {

    private MessageListenerContainer container;

    @Override
    public boolean subscribed() {
        return container.isRunning();
    }

    @Override
    public void unsubscribe() {
        container.stop();
    }
}
