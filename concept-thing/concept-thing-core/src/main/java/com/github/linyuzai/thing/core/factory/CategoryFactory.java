package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.container.Categories;

public interface CategoryFactory {

    Category create();

    Categories createContainer();
}
