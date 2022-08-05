package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.exception.RabbitEventException;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MethodRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;

import java.lang.reflect.Method;

public abstract class AnnotationRabbitEventSubscriber extends DefaultRabbitEventSubscriber {

    @SneakyThrows
    @Override
    public MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint, EventContext context) {
        Method method = getClass().getMethod("addAnnotationOnThisMethod");
        RabbitListener annotation = method.getAnnotation(RabbitListener.class);
        if (annotation == null) {
            throw new RabbitEventException("No @RabbitListener found on method #addAnnotationOnThisMethod");
        }
        RabbitListenerEndpoint rle = new MethodRabbitListenerEndpoint();
        return endpoint.getListenerContainerFactory().createListenerContainer(rle);
    }

    public abstract void addAnnotationOnThisMethod();
}
