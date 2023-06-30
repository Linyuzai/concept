package com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.rabbitmq.client.ShutdownNotifier;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.function.Consumer;

@Setter
@Getter
public class RabbitFanoutConnection extends AliveForeverConnection {

    private String id;

    private String exchange;

    private RabbitTemplate rabbitTemplate;

    public RabbitFanoutConnection(@NonNull String type) {
        super(type);
    }

    public RabbitFanoutConnection(@NonNull String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            rabbitTemplate.convertAndSend(exchange, "", message);
            onSuccess.run();
        } catch (AmqpIOException e) {
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            Boolean open = rabbitTemplate.execute(ShutdownNotifier::isOpen);
            if (open != null && open) {
                onSuccess.run();
            } else {
                onError.accept(new IllegalStateException("Rabbit ping: false"));
            }
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
