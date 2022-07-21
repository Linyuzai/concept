package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
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
                onPublishFailure(e, endpoint, context);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> result) {
                onPublishSuccess(result, endpoint, context);
            }
        });
    }

    public void onPublishSuccess(SendResult<Object, Object> result, KafkaEventEndpoint endpoint, EventContext context) {

    }

    public void onPublishFailure(Throwable e, KafkaEventEndpoint endpoint, EventContext context) {
        errorHandler.onError(e, endpoint, context);
    }
}
