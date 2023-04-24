package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.factory.AttributeFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;

@Setter
@Getter
public abstract class AbstractAttributes extends AbstractContainer<Attribute> implements Attributes {

    private Thing thing;

    @Override
    public ThingAction add(Label label) {
        return add(label, null);
    }

    @Override
    public ThingAction add(Label label, Function<Attribute, ThingAction> next) {
        Attribute attribute = create(label);
        ThingAction action = add(attribute);
        return chain(action, next, attribute);
    }


    protected Attribute create(Label label) {
        AttributeFactory factory = getContext().get(AttributeFactory.class);
        Attribute attribute = factory.create();
        attribute.setLabel(label);
        attribute.setThing(thing);
        attribute.setContext(getContext());
        return attribute;
    }

    @Override
    public ThingAction update(Map<String, Object> values) {
        ThingActionChain chain = chain();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Attribute attribute = get(key);
            //如果没有找到去 Category 中找对应的 Label
            if (attribute == null) {
                Label label = getLabelFromCategories(Collections.singletonList(getThing().getCategory()), key);
                if (label == null) {
                    throw new IllegalArgumentException("Label not found: " + key);
                }
                ThingAction add = add(label, attr -> attr.update(value));
                chain.next(add);
            } else {
                ThingAction update = attribute.update(value);
                chain.next(update);
            }
        }
        return chain;
    }

    protected Label getLabelFromCategories(List<Category> categories, String id) {
        List<Category> children = new ArrayList<>(categories);
        while (!children.isEmpty()) {
            List<Category> parents = new ArrayList<>();
            for (Category category : children) {
                parents.addAll(category.getParents().list());
                Label label = category.getLabels().get(id);
                if (label != null) {
                    return label;
                }
            }
            children.clear();
            children.addAll(parents);
        }
        return null;
    }
}
