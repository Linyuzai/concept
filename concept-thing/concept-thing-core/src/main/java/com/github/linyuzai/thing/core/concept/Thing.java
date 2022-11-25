package com.github.linyuzai.thing.core.concept;

public interface Thing {

    String getId();

    String getName();

    Categories getCategories();

    Attributes getAttributes();

    States getStates();
}
