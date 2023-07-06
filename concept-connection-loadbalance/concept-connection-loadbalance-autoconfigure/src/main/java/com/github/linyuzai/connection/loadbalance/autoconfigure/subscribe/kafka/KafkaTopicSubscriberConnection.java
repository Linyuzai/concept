package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.Map;
import java.util.function.Consumer;

@Setter
@Getter
public class KafkaTopicSubscriberConnection extends AliveForeverConnection {

    private String id;

    private MessageListenerContainer container;

    public KafkaTopicSubscriberConnection() {
        super(Type.SUBSCRIBER);
    }

    public KafkaTopicSubscriberConnection(Map<Object, Object> metadata) {
        super(Type.SUBSCRIBER, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            if (container != null && container.isRunning()) {
                container.stop();
            }
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
