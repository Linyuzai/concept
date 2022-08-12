package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.subscriber.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.kafka.listener.MessageListenerContainer;

/**
 * Kafka 订阅句柄
 */
@Getter
@AllArgsConstructor
public class KafkaSubscription implements Subscription {

    /**
     * 消息监听器容器
     */
    private MessageListenerContainer container;

    /**
     * 取消订阅
     * <p>
     * 停止容器
     */
    @Override
    public void unsubscribe() {
        if (container.isRunning()) {
            container.stop();
        }
    }
}
