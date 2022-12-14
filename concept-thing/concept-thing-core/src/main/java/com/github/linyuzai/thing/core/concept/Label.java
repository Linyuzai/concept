package com.github.linyuzai.thing.core.concept;

public interface Label extends Identify<Label> {

    String getName();

    void setName(String name);

    Category getCategory();

    void setCategory(Category category);
}
