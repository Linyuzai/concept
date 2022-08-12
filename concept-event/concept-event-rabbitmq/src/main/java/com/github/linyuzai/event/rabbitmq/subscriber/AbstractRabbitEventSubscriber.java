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

/**
 * RabbitMQ 事件订阅器抽象类
 */
public abstract class AbstractRabbitEventSubscriber extends RabbitEventSubscriber {

    /**
     * 在订阅之前先进行绑定
     */
    @Override
    public Subscription doSubscribe(EventListener listener, RabbitEventEndpoint endpoint, EventContext context) {
        binding(new RabbitBinding(endpoint.getAdmin()));
        MessageListener messageListener = createMessageListener(listener, endpoint, context);
        //使用SimpleRabbitListenerEndpoint需要设置监听器，否则会报错
        MessageListenerContainer container = createMessageListenerContainer(endpoint, context, messageListener);
        //如果没有设置监听器则设置生成的监听器
        if (container.getMessageListener() == null) {
            container.setupMessageListener(messageListener);
        }
        container.start();
        return new RabbitSubscription(container);
    }

    /**
     * 用于创建 交换机/队列/绑定关系
     */
    public void binding(RabbitBinding binding) {

    }

    /**
     * 创建消息监听器容器
     */
    public abstract MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint,
                                                                            EventContext context,
                                                                            MessageListener messageListener);

    /**
     * 创建消息监听器
     */
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

    /**
     * 根据 acknowledgeMode 和 batchEnabled 创建消息监听器
     */
    protected MessageListener createMessageListener(AcknowledgeMode acknowledgeMode,
                                                    boolean batchEnabled,
                                                    EventListener listener,
                                                    RabbitEventEndpoint endpoint,
                                                    EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        if (acknowledgeMode == AcknowledgeMode.MANUAL) {
            if (batchEnabled) {
                //手动&批量
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
                //手动&单条
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
                //自动&批量
                return (BatchMessageListener) messages -> {
                    for (Message message : messages) {
                        handleMessage(message, listener, endpoint, context, null);
                    }
                };
            } else {
                //自动&单条
                return message -> handleMessage(message, listener, endpoint, context, null);
            }
        }
    }

    /**
     * 处理消息
     */
    public void handleMessage(Message message,
                              EventListener listener,
                              RabbitEventEndpoint endpoint,
                              EventContext context,
                              Runnable runnable) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            //回调
            listener.onEvent(getPayload(message, endpoint, context), endpoint, context);
            //后置执行，用于手动ack
            if (runnable != null) {
                runnable.run();
            }
        } catch (Throwable e) {
            errorHandler.onError(e, endpoint, context);
        }
    }

    /**
     * 获得数据内容
     */
    public Object getPayload(Message message, RabbitEventEndpoint endpoint, EventContext context) {
        return message.getBody();
    }
}
