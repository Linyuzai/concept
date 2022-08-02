package com.github.linyuzai.event.core.bus;

import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.core.template.EventTemplate;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

@Getter
@Setter
public abstract class AbstractEventBus implements EventBus {

    private EventTemplate template;

    private Subscription subscription;

    @Override
    public void initialize() {
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
