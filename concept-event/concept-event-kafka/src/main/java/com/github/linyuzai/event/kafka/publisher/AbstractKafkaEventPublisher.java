package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public abstract class AbstractKafkaEventPublisher extends KafkaEventPublisher {

    @Override
    public void publishKafka(Object event, KafkaEventEndpoint endpoint, EventContext context) {
        ListenableFuture<SendResult<Object, Object>> send = send(event, endpoint, context);
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

    public abstract ListenableFuture<SendResult<Object, Object>> send(Object event, KafkaEventEndpoint endpoint, EventContext context);

    public void onPublishSuccess(SendResult<Object, Object> result, KafkaEventEndpoint endpoint, EventContext context) {

    }

    public void onPublishFailure(Throwable e, KafkaEventEndpoint endpoint, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        errorHandler.onError(e, endpoint, context);
    }
}
