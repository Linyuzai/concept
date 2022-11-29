package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.action.ThingActionChainFactory;
import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Relationships;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingEventPublisher;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractThing implements Thing, Thing.Modifiable {

    private String id;

    private String key;

    private String name;

    private Categories categories;

    private Attributes attributes;

    private Relationships relationships;

    private ThingContext context;

    @Override
    public ThingActionChain actions() {
        ThingActionChain chain = context.get(ThingActionChain.class);
        if (chain == null) {
            ThingActionChainFactory factory = context.get(ThingActionChainFactory.class);
            ThingActionChain create = factory.create(this);
            context.put(ThingActionChain.class, create);
            return create;
        } else {
            return chain;
        }
    }

    @Override
    public void publish(Object event) {
        ThingEventPublisher publisher = context.get(ThingEventPublisher.class);
        publisher.publish(event);
    }

    @Override
    public <T> T create(Class<T> target) {
        return null;
    }
}
