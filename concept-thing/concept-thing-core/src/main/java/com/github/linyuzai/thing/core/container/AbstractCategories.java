package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractCategories extends AbstractContainer<Category> implements Categories, Categories.Modifiable {

    private Category category;

    @Override
    protected ThingContext getContext() {
        return category.getContext();
    }

    @Override
    protected void onPrepare(Category add) {

    }

    @Override
    protected void onValid(Category add) {
        
    }
}
