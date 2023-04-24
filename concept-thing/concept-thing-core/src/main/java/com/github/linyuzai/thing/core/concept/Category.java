package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Labels;

public interface Category extends Identify {

    String getName();

    void setName(String name);

    Categories getParents();

    void setParents(Categories categories);

    Labels getLabels();

    void setLabels(Labels labels);
}
