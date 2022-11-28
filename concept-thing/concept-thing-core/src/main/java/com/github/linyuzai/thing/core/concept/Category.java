package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Labels;

public interface Category extends IdAndKey {

    String getName();

    Category getParent();

    Categories getCategories();

    Labels getLabels();

    interface Modifiable extends IdAndKey.Modifiable {

        void setName(String name);

        void setParent(Category parent);

        void setCategories(Categories categories);

        void setLabels(Labels labels);
    }
}
