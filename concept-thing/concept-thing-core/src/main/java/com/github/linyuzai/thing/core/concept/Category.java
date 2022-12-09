package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Labels;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface Category extends Identify {

    String getName();

    void setName(String name);

    Category getParent();

    void setParent(Category parent);

    Categories getCategories();

    void setCategories(Categories categories);

    Labels getLabels();

    void setLabels(Labels labels);

    ThingContext getContext();

    void setContext(ThingContext context);
}
