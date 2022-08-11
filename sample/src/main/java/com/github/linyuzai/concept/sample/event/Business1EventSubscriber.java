package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.kafka.subscriber.KafkaSubscription;
import com.github.linyuzai.event.rabbitmq.subscriber.RabbitSubscription;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;

public class Business1EventSubscriber implements EventSubscriber {

    @Override
    public Subscription subscribe(EventListener listener, EventEndpoint endpoint, EventContext context) {
        if (endpoint.getName().equals("kafka1")) {
            ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            MessageListenerContainer container = factory.createContainer("topic");
            ContainerProperties properties = container.getContainerProperties();
            properties.setMessageListener(new org.springframework.kafka.listener.MessageListener<Object, Object>() {
                @Override
                public void onMessage(ConsumerRecord<Object, Object> data) {
                    listener.onEvent(data.value(), endpoint, context);
                }
            });
            container.start();
            return new KafkaSubscription(container);
        }
        if (endpoint.getName().equals("rabbitmq2")) {
            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
            SimpleRabbitListenerEndpoint listenerEndpoint = new SimpleRabbitListenerEndpoint();
            listenerEndpoint.setQueueNames("queue");
            listenerEndpoint.setMessageListener(new org.springframework.amqp.core.MessageListener() {
                @Override
                public void onMessage(Message message) {
                    listener.onEvent(message.getBody(), endpoint, context);
                }
            });
            SimpleMessageListenerContainer container = factory.createListenerContainer(listenerEndpoint);
            container.start();
            return new RabbitSubscription(container);
        }
        return Subscription.EMPTY;
    }
}
