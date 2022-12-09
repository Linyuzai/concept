package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.CategoryImpl;
import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.CategoriesImpl;

public class CategoryFactoryImpl implements CategoryFactory {

    @Override
    public Category create() {
        return new CategoryImpl();
    }

    @Override
    public Categories createContainer() {
        return new CategoriesImpl();
    }
}
