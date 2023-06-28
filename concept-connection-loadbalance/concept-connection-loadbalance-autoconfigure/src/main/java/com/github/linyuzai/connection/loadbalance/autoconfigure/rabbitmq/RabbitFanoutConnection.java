package com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
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
    public void doSend(Object message, Runnable success, Consumer<Throwable> error) {
        try {
            rabbitTemplate.convertAndSend(exchange, "", message);
            success.run();
        } catch (Throwable e) {
            error.accept(new MessageTransportException(e));
        }
    }
}
