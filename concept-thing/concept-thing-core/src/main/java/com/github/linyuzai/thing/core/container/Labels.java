package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Label;

public interface Labels extends Container<Label> {

    Category getCategory();

    interface Modifiable {

        void setCategory(Category category);
    }
}
