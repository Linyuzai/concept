package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.rabbitmq.client.ShutdownNotifier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.function.Consumer;

/**
 * RabbitMQ 转发连接。
 * <p>
 * The connection to forward message by RabbitMQ.
 */
@Setter
@Getter
public class RabbitFanoutObservableConnection extends AliveForeverConnection {

    private String id;

    private String exchange;

    private RabbitTemplate rabbitTemplate;

    public RabbitFanoutObservableConnection() {
        super(Type.OBSERVABLE);
    }

    public RabbitFanoutObservableConnection(Map<Object, Object> metadata) {
        super(Type.OBSERVABLE, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            rabbitTemplate.convertAndSend(exchange, "", message);
            onSuccess.run();
        } catch (AmqpException e) {
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

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }
}
