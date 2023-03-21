package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingAction;
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

    private Categories parents;

    private Labels labels;

    @Override
    public ThingAction addLabel(String key, String name) {
        return addLabel(getKey() + key, key, name);
    }

    @Override
    public ThingAction addLabel(String id, String key, String name) {
        return ThingAction.create(() -> {
            doAddLabel(id, key, name);
            //TODO event
            return () -> null;
        });
    }

    protected void doAddLabel(String id, String key, String name) {
        LabelFactory factory = getContext().get(LabelFactory.class);
        Label label = factory.create();
        label.setId(id);
        label.setKey(key);
        label.setName(name);
        label.setCategory(this);
        label.setContext(getContext());
    }

    @Override
    public Categories getParents() {
        if (parents == null) {
            CategoryFactory factory = getContext().get(CategoryFactory.class);
            parents = factory.createContainer();
            parents.setContext(getContext());
        }
        return parents;
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
