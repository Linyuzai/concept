package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Relationship;
import com.github.linyuzai.thing.core.context.ThingContext;

public abstract class AbstractRelationships extends AbstractContainer<Relationship> implements Relationships {

    @Override
    protected ThingContext getContext() {
        return getThing().getContext();
    }
}
