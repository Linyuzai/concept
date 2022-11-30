package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.event.ThingEvent;
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
    protected void onAdd(Category add) {
        categories.put(add.getId(), add);
    }

    @Override
    protected ThingEvent createAddedEvent(Category add) {
        return null;
    }

    @Override
    protected Category onRemove(String id) {
        return categories.remove(id);
    }

    @Override
    protected ThingEvent createRemovedEvent(String id, Category removed) {
        return null;
    }
}
