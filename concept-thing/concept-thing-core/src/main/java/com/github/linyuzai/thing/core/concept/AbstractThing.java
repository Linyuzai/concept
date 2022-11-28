package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.action.ThingActionChainFactory;
import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Relationships;
import com.github.linyuzai.thing.core.container.States;
import com.github.linyuzai.thing.core.context.ThingContext;
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

    private States states;

    private Relationships relationships;

    private ThingContext context;

    @Override
    public ThingActionChain action(ThingAction action) {
        ThingActionChainFactory factory = context.get(ThingActionChainFactory.class);
        ThingActionChain chain = factory.create(this);
        return chain.action(action);
    }

    @Override
    public <T> T create(Class<T> target) {
        return null;
    }
}
