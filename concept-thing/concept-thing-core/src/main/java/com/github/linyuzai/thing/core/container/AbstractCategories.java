package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
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
    public ThingAction add(String name) {
        return add(name, null);
    }

    @Override
    public ThingAction add(String name, Function<Category, ThingAction> next) {
        Category category = create(name);
        ThingAction action = add(category);
        if (next == null) {
            return action;
        } else {
            ThingAction apply = next.apply(category);
            ThingActionChain chain = getContext().actions();
            return chain.next(action).next(apply);
        }
    }

    protected Category create(String name) {
        CategoryFactory factory = getContext().get(CategoryFactory.class);
        Category category = factory.create();
        category.setName(name);
        category.setParent(getCategory());
        category.setContext(getContext());
        return category;
    }
}
