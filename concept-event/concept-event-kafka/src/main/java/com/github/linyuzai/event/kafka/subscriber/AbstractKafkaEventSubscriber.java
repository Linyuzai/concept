package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.AllArgsConstructor;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public abstract class AbstractKafkaEventSubscriber extends KafkaEventSubscriber {

    @Override
    public void subscribeKafka(KafkaEventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        MessageListenerContainer container = createContainer(endpoint, context);
        container.getContainerProperties().setMessageListener(createMessageListener(endpoint, context, consumer));
        container.start();
        EventConcept concept = context.get(EventConcept.class);
        concept.addLifecycleListeners(new MessageListenerContainerStopper(container));
    }

    public abstract MessageListenerContainer createContainer(KafkaEventEndpoint endpoint, EventContext context);

    public abstract MessageListener<?, ?> createMessageListener(KafkaEventEndpoint endpoint, EventContext context, Consumer<Object> consumer);

    @AllArgsConstructor
    public static class MessageListenerContainerStopper implements EventConceptLifecycleListener {

        private MessageListenerContainer container;

        @Override
        public void onInitialize(EventConcept concept) {

        }

        @Override
        public void onDestroy(EventConcept concept) {
            if (container.isRunning()) {
                container.stop();
            }
        }
    }
}
