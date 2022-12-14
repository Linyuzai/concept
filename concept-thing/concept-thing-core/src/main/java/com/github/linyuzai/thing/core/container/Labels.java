package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Label;

import java.util.function.Function;

public interface Labels extends Container<Label> {

    Category getCategory();

    void setCategory(Category category);

    ThingAction add(String name);

    ThingAction add(String name, Function<Label, ThingAction> next);
}
