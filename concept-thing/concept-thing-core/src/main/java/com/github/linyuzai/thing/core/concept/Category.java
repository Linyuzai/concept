package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Labels;

public interface Category extends Identify<Category> {

    String getName();

    void setName(String name);

    Categories getParents();

    void setParents(Categories categories);

    Labels getLabels();

    void setLabels(Labels labels);

    ThingAction addLabel(String key, String name);

    ThingAction addLabel(String id, String key, String name);
}
