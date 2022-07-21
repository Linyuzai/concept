package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import lombok.AllArgsConstructor;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.lang.reflect.Type;

public abstract class AbstractKafkaEventSubscriber<T> implements KafkaEventSubscriber<T> {

    @Override
    public void subscribe(Type type, KafkaEventEndpoint endpoint, EventContext context) {
        MessageListenerContainer container = createContainer(type, endpoint, context);
        container.getContainerProperties().setMessageListener(createMessageListener(type, endpoint, context));
        container.start();
        EventConcept concept = context.get(EventConcept.class);
        concept.addLifecycleListeners(new MessageListenerContainerStopper(container));
    }

    public abstract MessageListenerContainer createContainer(Type type, KafkaEventEndpoint endpoint, EventContext context);

    public abstract MessageListener<?, ?> createMessageListener(Type type, KafkaEventEndpoint endpoint, EventContext context);

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
