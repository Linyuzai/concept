package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.rabbitmq.binding.RabbitBinding;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.exception.RabbitEventException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BatchMessageListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractRabbitEventSubscriber extends RabbitEventSubscriber {

    @Override
    public Subscription subscribeRabbit(RabbitEventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        MessageListenerContainer container = createMessageListenerContainer(endpoint, context);
        binding(new RabbitBinding(endpoint.getAdmin()));
        if (container instanceof AbstractMessageListenerContainer) {
            ((AbstractMessageListenerContainer) container).setAmqpAdmin(endpoint.getAdmin());
        }
        container.setupMessageListener(createMessageListener(endpoint, context, consumer));
        container.start();
        return new RabbitSubscription(container);
    }

    public void binding(RabbitBinding binding) {

    }

    public abstract MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint, EventContext context);

    public MessageListener createMessageListener(RabbitEventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        RabbitProperties.Listener listener = endpoint.getProperties().getListener();
        if (listener.getType() == RabbitProperties.ContainerType.SIMPLE) {
            RabbitProperties.SimpleContainer simple = listener.getSimple();
            AcknowledgeMode acknowledgeMode = simple.getAcknowledgeMode();
            boolean batchEnabled = simple.isConsumerBatchEnabled();
            return createMessageListener(acknowledgeMode, batchEnabled, endpoint, context, consumer);
        }
        if (listener.getType() == RabbitProperties.ContainerType.DIRECT) {
            RabbitProperties.DirectContainer direct = listener.getDirect();
            AcknowledgeMode acknowledgeMode = direct.getAcknowledgeMode();
            return createMessageListener(acknowledgeMode, false, endpoint, context, consumer);
        }
        throw new RabbitEventException("Unsupported listener type: " + listener.getType());
    }

    protected MessageListener createMessageListener(AcknowledgeMode acknowledgeMode,
                                                    boolean batchEnabled,
                                                    RabbitEventEndpoint endpoint,
                                                    EventContext context,
                                                    Consumer<Object> consumer) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        if (acknowledgeMode == AcknowledgeMode.MANUAL) {
            if (batchEnabled) {
                return (ChannelAwareBatchMessageListener) (messages, channel) -> {
                    for (Message message : messages) {
                        handleMessage(message, endpoint, context, () -> {
                            try {
                                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                            } catch (Throwable e) {
                                errorHandler.onError(e, endpoint, context);
                            }
                        }, consumer);
                    }
                };
            } else {
                return (ChannelAwareMessageListener) (message, channel) -> {
                    handleMessage(message, endpoint, context, () -> {
                        try {
                            Objects.requireNonNull(channel).basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        } catch (Throwable e) {
                            errorHandler.onError(e, endpoint, context);
                        }
                    }, consumer);
                };
            }
        } else {
            if (batchEnabled) {
                return (BatchMessageListener) messages -> {
                    for (Message message : messages) {
                        handleMessage(message, endpoint, context, null, consumer);
                    }
                };
            } else {
                return message -> handleMessage(message, endpoint, context, null, consumer);
            }
        }
    }

    public void handleMessage(Message message,
                              RabbitEventEndpoint endpoint,
                              EventContext context,
                              Runnable runnable,
                              Consumer<Object> consumer) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            consumer.accept(getPayload(message, endpoint, context));
            if (runnable != null) {
                runnable.run();
            }
        } catch (Throwable e) {
            errorHandler.onError(e, endpoint, context);
        }
    }

    public Object getPayload(Message message, RabbitEventEndpoint endpoint, EventContext context) {
        return message.getBody();
    }
}
