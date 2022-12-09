package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.context.ThingContext;

public abstract class AbstractLabels extends AbstractContainer<Label> implements Labels {

    public ThingContext getContext() {
        return getCategory().getContext();
    }
}
