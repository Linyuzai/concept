package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
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
    public ThingAction add(String name) {
        return add(name, null);
    }

    @Override
    public ThingAction add(String name, Function<Label, ThingAction> next) {
        Label label = create(name);
        ThingAction action = add(label);
        if (next == null) {
            return action;
        } else {
            ThingAction apply = next.apply(label);
            ThingActionChain chain = getContext().actions();
            return chain.next(action).next(apply);
        }
    }

    protected Label create(String name) {
        LabelFactory factory = getContext().get(LabelFactory.class);
        Label label = factory.create();
        label.setName(name);
        label.setCategory(category);
        label.setContext(getContext());
        return label;
    }
}
