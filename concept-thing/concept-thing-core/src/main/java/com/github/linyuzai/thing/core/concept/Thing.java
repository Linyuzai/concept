package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.container.*;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface Thing extends IdAndKey {

    String getName();

    Categories getCategories();

    Attributes getAttributes();

    States getStates();

    Relationships getRelationships();

    ThingContext getContext();

    ThingActionChain action(ThingAction action);

    <T> T create(Class<T> target);

    interface Modifiable extends IdAndKey.Modifiable {

        void setName(String name);

        void setCategories(Categories categories);

        void setAttributes(Attributes attributes);

        void setStates(States states);

        void setRelationships(Relationships relationships);

        void setContext(ThingContext context);
    }
}
