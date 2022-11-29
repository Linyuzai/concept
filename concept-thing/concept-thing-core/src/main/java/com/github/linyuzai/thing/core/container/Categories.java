package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface Categories extends Container<Category> {

    ThingContext getContext();

    interface Modifiable {

        void setContext(ThingContext context);
    }
}
