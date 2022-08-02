package com.github.linyuzai.event.core.bus;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.config.AbstractInstanceConfig;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.core.template.EventTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Type;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractEventBus extends AbstractInstanceConfig implements EventBus {

    private final EventConcept concept;

    private final EventExchange exchange;

    private EventTemplate template;

    private Subscription subscription;

    @Override
    public void initialize() {
        if (subscription != null && subscription.subscribed()) {
            return;
        }
        template = concept.template()
                .exchange(exchange)
                .encoder(getEncoder())
                .decoder(getDecoder())
                .publisher(getPublisher())
                .subscriber(getSubscriber())
                .error(getErrorHandler());
        subscription = subscribe();
    }

    @Override
    public void destroy() {
        if (subscription != null && subscription.subscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void publish(Object event) {
        template.publish(event);
        onPublish(event);
    }

    public Subscription subscribe() {
        return template.subscribe(new EventListener() {
            @Override
            public void onEvent(Object event) {
                AbstractEventBus.this.onEvent(event);
            }

            @Override
            public Type getType() {
                return null;
            }
        });
    }

    public abstract void onPublish(Object event);

    public abstract void onEvent(Object event);
}
