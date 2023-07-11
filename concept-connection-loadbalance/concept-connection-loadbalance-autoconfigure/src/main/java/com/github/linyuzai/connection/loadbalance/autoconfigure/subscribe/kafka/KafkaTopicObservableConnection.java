package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Kafka Topic 转发连接。
 * <p>
 * The connection to forward message by Kafka.
 */
@Setter
@Getter
public class KafkaTopicObservableConnection extends AliveForeverConnection {

    private String id;

    private String topic;

    private KafkaTemplate<?, Object> kafkaTemplate;

    public KafkaTopicObservableConnection() {
        super(Type.OBSERVABLE);
    }

    public KafkaTopicObservableConnection(Map<Object, Object> metadata) {
        super(Type.OBSERVABLE, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        ListenableFuture<? extends SendResult<?, Object>> send;
        try {
            send = kafkaTemplate.send(topic, message);
        } catch (KafkaException | org.apache.kafka.common.KafkaException e) {
            onError.accept(new MessageTransportException(e));
            onComplete.run();
            return;
        } catch (Throwable e) {
            onError.accept(e);
            onComplete.run();
            return;
        }
        send.addCallback(new ListenableFutureCallback<SendResult<?, Object>>() {

            @Override
            public void onFailure(Throwable e) {
                if (e instanceof KafkaException || e instanceof org.apache.kafka.common.KafkaException) {
                    onError.accept(new MessageTransportException(e));
                } else {
                    onError.accept(e);
                }
                onComplete.run();
            }

            @Override
            public void onSuccess(SendResult<?, Object> result) {
                onSuccess.run();
                onComplete.run();
            }
        });
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            Map<MetricName, ? extends Metric> metrics = kafkaTemplate.metrics();
            onSuccess.run();
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }
}
