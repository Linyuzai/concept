package com.github.linyuzai.thing.core.concept;

public interface Category {

    String getId();

    String getName();

    Category getCategory();

    Categories getCategories();

    Labels getLabels();
}
