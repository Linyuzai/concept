package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Labels;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface Category extends IdAndKey {

    String getName();

    Category getParent();

    Categories getCategories();

    Labels getLabels();

    ThingContext getContext();

    interface Modifiable extends IdAndKey.Modifiable {

        void setName(String name);

        void setParent(Category parent);

        void setCategories(Categories categories);

        void setLabels(Labels labels);

        void setContext(ThingContext context);
    }
}
