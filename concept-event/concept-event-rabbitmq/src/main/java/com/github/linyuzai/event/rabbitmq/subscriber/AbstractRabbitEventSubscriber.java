package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.rabbitmq.binding.RabbitBinding;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import com.github.linyuzai.event.rabbitmq.exception.RabbitEventException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BatchMessageListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.util.Objects;

public abstract class AbstractRabbitEventSubscriber extends RabbitEventSubscriber {

    @Override
    public Subscription doSubscribe(EventListener listener, RabbitEventEndpoint endpoint, EventContext context) {
        binding(new RabbitBinding(endpoint.getAdmin()));
        MessageListener messageListener = createMessageListener(listener, endpoint, context);
        MessageListenerContainer container = createMessageListenerContainer(endpoint, context, messageListener);
        if (container.getMessageListener() == null) {
            container.setupMessageListener(messageListener);
        }
        container.start();
        return new RabbitSubscription(container);
    }

    public void binding(RabbitBinding binding) {

    }

    public abstract MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint,
                                                                            EventContext context,
                                                                            MessageListener messageListener);

    public MessageListener createMessageListener(EventListener listener, RabbitEventEndpoint endpoint, EventContext context) {
        RabbitProperties.Listener listenerProperties = endpoint.getProperties().getListener();
        if (listenerProperties.getType() == RabbitProperties.ContainerType.SIMPLE) {
            RabbitProperties.SimpleContainer simple = listenerProperties.getSimple();
            AcknowledgeMode acknowledgeMode = simple.getAcknowledgeMode();
            boolean batchEnabled = simple.isConsumerBatchEnabled();
            return createMessageListener(acknowledgeMode, batchEnabled, listener, endpoint, context);
        }
        if (listenerProperties.getType() == RabbitProperties.ContainerType.DIRECT) {
            RabbitProperties.DirectContainer direct = listenerProperties.getDirect();
            AcknowledgeMode acknowledgeMode = direct.getAcknowledgeMode();
            return createMessageListener(acknowledgeMode, false, listener, endpoint, context);
        }
        throw new RabbitEventException("Unsupported listener type: " + listener.getType());
    }

    protected MessageListener createMessageListener(AcknowledgeMode acknowledgeMode,
                                                    boolean batchEnabled,
                                                    EventListener listener,
                                                    RabbitEventEndpoint endpoint,
                                                    EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        if (acknowledgeMode == AcknowledgeMode.MANUAL) {
            if (batchEnabled) {
                return (ChannelAwareBatchMessageListener) (messages, channel) -> {
                    for (Message message : messages) {
                        handleMessage(message, listener, endpoint, context, () -> {
                            try {
                                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                            } catch (Throwable e) {
                                errorHandler.onError(e, endpoint, context);
                            }
                        });
                    }
                };
            } else {
                return (ChannelAwareMessageListener) (message, channel) ->
                        handleMessage(message, listener, endpoint, context, () -> {
                            try {
                                Objects.requireNonNull(channel)
                                        .basicAck(message.getMessageProperties().getDeliveryTag(),
                                                false);
                            } catch (Throwable e) {
                                errorHandler.onError(e, endpoint, context);
                            }
                        });
            }
        } else {
            if (batchEnabled) {
                return (BatchMessageListener) messages -> {
                    for (Message message : messages) {
                        handleMessage(message, listener, endpoint, context, null);
                    }
                };
            } else {
                return message -> handleMessage(message, listener, endpoint, context, null);
            }
        }
    }

    public void handleMessage(Message message,
                              EventListener listener,
                              RabbitEventEndpoint endpoint,
                              EventContext context,
                              Runnable runnable) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            listener.onEvent(getPayload(message, endpoint, context), endpoint, context);
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
