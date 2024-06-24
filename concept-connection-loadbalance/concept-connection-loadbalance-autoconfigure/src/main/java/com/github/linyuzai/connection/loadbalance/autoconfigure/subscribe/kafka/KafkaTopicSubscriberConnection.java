package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.function.Consumer;

/**
 * Kafka Topic 监听连接。
 * <p>
 * The connection to listen message from Kafka.
 */
@Setter
@Getter
public class KafkaTopicSubscriberConnection extends AliveForeverConnection {

    private String id;

    private MessageListenerContainer container;

    public KafkaTopicSubscriberConnection() {
        setType(Type.SUBSCRIBER);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    /**
     * 在连接关闭的时候停止监听。
     * <p>
     * Stop listen when closing.
     */
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
