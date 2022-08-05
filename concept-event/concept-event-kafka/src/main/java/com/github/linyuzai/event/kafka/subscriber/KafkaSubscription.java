package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.subscriber.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

@Getter
@AllArgsConstructor
public class KafkaSubscription implements Subscription {

    private MessageListenerContainer container;

    @Override
    public void unsubscribe() {
        if (container.isRunning()) {
            container.stop();
        }
    }
}
