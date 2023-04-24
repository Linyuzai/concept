package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.*;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface Thing extends Identify {

    String getName();

    void setName(String name);

    Category getCategory();

    void setCategory(Category category);

    Attributes getAttributes();

    void setAttributes(Attributes attributes);

    Relationships getRelationships();

    void setRelationships(Relationships relationships);

    ThingContext getContext();

    void setContext(ThingContext context);

    void publish(Object event);

    <T> T create(Class<T> target);
}
