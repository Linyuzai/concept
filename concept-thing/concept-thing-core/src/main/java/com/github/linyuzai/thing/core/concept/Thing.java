package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.container.*;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface Thing extends IdAndKey {

    String getName();

    Categories getCategories();

    Attributes getAttributes();

    Relationships getRelationships();

    ThingContext getContext();

    ThingActionChain actions();

    void publish(Object event);

    <T> T create(Class<T> target);

    interface Modifiable extends IdAndKey.Modifiable {

        void setName(String name);

        void setCategories(Categories categories);

        void setAttributes(Attributes attributes);

        void setRelationships(Relationships relationships);

        void setContext(ThingContext context);
    }
}
