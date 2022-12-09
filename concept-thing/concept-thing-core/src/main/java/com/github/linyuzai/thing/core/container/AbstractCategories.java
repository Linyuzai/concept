package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.context.ThingContext;

public abstract class AbstractCategories extends AbstractContainer<Category> implements Categories {

    @Override
    protected ThingContext getContext() {
        return getCategory().getContext();
    }
}
