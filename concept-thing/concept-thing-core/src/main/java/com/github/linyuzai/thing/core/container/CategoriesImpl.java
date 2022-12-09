package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CategoriesImpl extends AbstractCategories {

    private Category category;

    private Map<String, Category> map;

    @Override
    protected ThingEvent createAddedEvent(Category add) {
        return null;
    }

    @Override
    protected ThingEvent createRemovedEvent(String id, Category removed) {
        return null;
    }
}
