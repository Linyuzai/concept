package com.github.linyuzai.thing.core.concept;

public interface Label extends IdAndKey {

    String getName();

    Category getCategory();

    interface Modifiable extends IdAndKey.Modifiable {

        void setName(String name);

        void setCategory(Category category);
    }
}
