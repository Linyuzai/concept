package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.exception.RabbitEventException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MethodRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;

import java.lang.reflect.Method;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationRabbitEventSubscriber extends AbstractRabbitEventSubscriber {

    private Object target;

    @Override
    public MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint,
                                                                   EventContext context,
                                                                   MessageListener messageListener) {
        return endpoint.getListenerContainerFactory().createListenerContainer(generateRabbitListenerEndpoint());
    }

    @SneakyThrows
    public RabbitListenerEndpoint generateRabbitListenerEndpoint() {
        Class<?> targetClass = target == null ? getClass() : target.getClass();
        Method method = targetClass.getMethod("addAnnotationOnThisMethod");
        RabbitListener annotation = method.getAnnotation(RabbitListener.class);
        if (annotation == null) {
            throw new RabbitEventException("No @RabbitListener found on method #addAnnotationOnThisMethod");
        }
        return new MethodRabbitListenerEndpoint();
    }
}
