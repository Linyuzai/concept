package com.github.linyuzai.connection.loadbalance.autoconfigure.kafka;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.function.Consumer;

@Setter
@Getter
public class KafkaTopicConnection extends AliveForeverConnection {

    private String id;

    private String topic;

    private KafkaTemplate<?, Object> kafkaTemplate;

    public KafkaTopicConnection(@NonNull String type) {
        super(type);
    }

    public KafkaTopicConnection(@NonNull String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        kafkaTemplate.send(topic, message).addCallback(new ListenableFutureCallback<SendResult<?, Object>>() {

            @Override
            public void onFailure(Throwable e) {
                if (e instanceof KafkaException) {
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
}
