package com.github.linyuzai.event.kafka.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Kafka 事件发布器抽象类
 */
public abstract class AbstractKafkaEventPublisher extends KafkaEventPublisher {

    @Override
    public void doPublish(Object event, KafkaEventEndpoint endpoint, EventContext context) {
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

    /**
     * 发送消息
     */
    public abstract ListenableFuture<SendResult<Object, Object>> send(Object event, KafkaEventEndpoint endpoint, EventContext context);

    /**
     * 成功回调
     */
    public void onPublishSuccess(SendResult<Object, Object> result, KafkaEventEndpoint endpoint, EventContext context) {
        //不做处理
    }

    /**
     * 失败回调
     * <p>
     * 调用 {@link EventErrorHandler} 处理异常
     */
    public void onPublishFailure(Throwable e, KafkaEventEndpoint endpoint, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        errorHandler.onError(e, endpoint, context);
    }
}
