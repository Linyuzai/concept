package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.factory.CategoryFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
public abstract class AbstractCategories extends AbstractContainer<Category> implements Categories {

    private Category category;

    @Override
    public ThingAction add(String id, String name) {
        return add(id, name, null);
    }

    @Override
    public ThingAction add(String id, String name, Function<Category, ThingAction> next) {
        Category category = create(id, name);
        ThingAction action = add(category);
        return chain(action, next, category);
    }

    protected Category create(String id, String name) {
        CategoryFactory factory = getContext().get(CategoryFactory.class);
        Category category = factory.create();
        category.setId(id);
        category.setName(name);
        category.setContext(getContext());
        return category;
    }
}
