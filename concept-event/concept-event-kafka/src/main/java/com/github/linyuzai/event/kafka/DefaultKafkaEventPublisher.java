package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import lombok.AllArgsConstructor;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@AllArgsConstructor
public class DefaultKafkaEventPublisher implements KafkaEventPublisher {

    private EventErrorHandler errorHandler;

    @Override
    public void publish(Object event, KafkaEventEndpoint endpoint, EventContext context) {
        ListenableFuture<SendResult<Object, Object>> send = endpoint.getTemplate().sendDefault(event);
        send.addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {

            @Override
            public void onFailure(@NonNull Throwable e) {
                errorHandler.onError(e, endpoint, context);
                //log.error("Event publish to kafka failure", e);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> result) {

            }
        });
    }
}
