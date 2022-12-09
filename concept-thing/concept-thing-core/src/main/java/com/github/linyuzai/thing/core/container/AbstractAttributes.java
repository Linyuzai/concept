package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.context.ThingContext;

public abstract class AbstractAttributes extends AbstractContainer<Attribute> implements Attributes {

    @Override
    protected ThingContext getContext() {
        return getThing().getContext();
    }
}
