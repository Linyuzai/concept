package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.container.*;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface Thing extends Identify<Thing> {

    String getName();

    void setName(String name);

    Categories getCategories();

    void setCategories(Categories categories);

    Attributes getAttributes();

    void setAttributes(Attributes attributes);

    Relationships getRelationships();

    void setRelationships(Relationships relationships);

    ThingContext getContext();

    void setContext(ThingContext context);

    ThingActionChain actions();

    void publish(Object event);

    <T> T create(Class<T> target);
}
