package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Labels;
import com.github.linyuzai.thing.core.context.ThingContext;

import java.util.function.Function;

public interface Category extends Identify<Category> {

    String getName();

    void setName(String name);

    Categories getCategories();

    void setCategories(Categories categories);

    Labels getLabels();

    void setLabels(Labels labels);
}
