package com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

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
    public void doSend(Object message) {
        rabbitTemplate.convertAndSend(exchange, "", message);
    }
}
