package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.inner.InnerThingAction;
import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Category;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CategoriesImpl extends AbstractCategories {

    private final Map<String, Category> categories;

    @Override
    public Category get(String id) {
        return categories.get(id);
    }

    @Override
    public List<Category> list() {
        return Collections.unmodifiableList(new ArrayList<>(categories.values()));
    }

    @Override
    public ThingAction add(Category one) {
        return new InnerThingAction(getContext(), () -> {
            categories.put(one.getId(), one);
            return null;
        });
    }

    @Override
    public ThingAction remove(String id) {
        return new InnerThingAction(getContext(), () -> {
            Category remove = categories.remove(id);
            return null;
        });
    }
}
