package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;

public interface Categories extends Container<Category> {

    Category getCategory();

    void setCategory(Category category);
}
