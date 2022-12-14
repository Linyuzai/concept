package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Labels;
import com.github.linyuzai.thing.core.factory.CategoryFactory;
import com.github.linyuzai.thing.core.factory.LabelFactory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractCategory extends AbstractIdentify<Category> implements Category {

    private String name;

    private Category parent;

    private Categories categories;

    private Labels labels;

    @Override
    public Categories getCategories() {
        if (categories == null) {
            CategoryFactory factory = getContext().get(CategoryFactory.class);
            categories = factory.createContainer();
            categories.setContext(getContext());
        }
        return categories;
    }

    @Override
    public Labels getLabels() {
        if (labels == null) {
            LabelFactory factory = getContext().get(LabelFactory.class);
            labels = factory.createContainer();
            labels.setContext(getContext());
            labels.setCategory(this);
        }
        return labels;
    }
}
