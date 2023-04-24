package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.factory.LabelFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
public abstract class AbstractLabels extends AbstractContainer<Label> implements Labels {

    private Category category;

    @Override
    public ThingAction add(String id, String name) {
        return add(id, name, null);
    }

    @Override
    public ThingAction add(String id, String name, Function<Label, ThingAction> next) {
        Label label = create(id, name);
        ThingAction action = add(label);
        return chain(action, next, label);
    }

    protected Label create(String id, String name) {
        LabelFactory factory = getContext().get(LabelFactory.class);
        Label label = factory.create();
        label.setId(id);
        label.setName(name);
        label.setContext(getContext());
        return label;
    }
}
