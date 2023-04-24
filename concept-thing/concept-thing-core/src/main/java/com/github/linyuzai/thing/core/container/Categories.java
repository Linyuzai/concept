package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Category;

import java.util.function.Function;

public interface Categories extends Container<Category> {

    Category getCategory();

    void setCategory(Category category);

    ThingAction add(String id, String name);

    ThingAction add(String id, String name, Function<Category, ThingAction> next);
}
