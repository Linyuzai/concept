package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface Labels extends Container<Label> {

    ThingContext getContext();

    interface Modifiable {

        void setContext(ThingContext context);
    }
}
