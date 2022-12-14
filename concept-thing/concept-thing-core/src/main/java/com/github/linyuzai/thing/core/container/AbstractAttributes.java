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
import java.util.stream.Collectors;

@Setter
@Getter
public abstract class AbstractAttributes extends AbstractContainer<Attribute> implements Attributes {

    private Thing thing;

    private Map<String, List<Label>> keyLabelsCache = new LinkedHashMap<>();

    @Override
    public ThingAction add(Label label) {
        return add(label, null);
    }

    @Override
    public ThingAction add(Label label, Function<Attribute, ThingAction> next) {
        Attribute attribute = create(label);
        ThingAction action = add(attribute);
        if (next == null) {
            return action;
        } else {
            ThingAction apply = next.apply(attribute);
            ThingActionChain chain = getContext().actions();
            return chain.next(action).next(apply);
        }
    }

    @Override
    public ThingAction add(String label) {
        return add(label, null);
    }

    @Override
    public ThingAction add(String label, Function<Attribute, ThingAction> next) {
        if (!keyLabelsCache.containsKey(label)) {
            collectLabels(getThing().getCategories(), Collections.singleton(label));
        }
        ThingActionChain chain = getContext().actions();
        List<Label> labels = keyLabelsCache.get(label);
        for (Label l : labels) {
            ThingAction add = add(l, next);
            chain.next(add);
        }
        return chain;
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
        ThingActionChain chain = getContext().actions();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Attribute attribute = get(key);
            //如果没有找到去 Category 中找对应的 Label
            if (attribute == null) {
                Set<String> collectKeys = values.keySet()
                        .stream()
                        .filter(it -> !keyLabelsCache.containsKey(it))
                        .collect(Collectors.toSet());
                if (!collectKeys.isEmpty()) {
                    collectLabels(getThing().getCategories(), collectKeys);
                    List<Label> labels = keyLabelsCache.get(key);
                    for (Label label : labels) {
                        ThingAction add = add(label, attr -> attr.update(value));
                        chain.next(add);
                    }
                }
            } else {
                ThingAction update = attribute.update(value);
                chain.next(update);
            }
        }
        return chain;
    }

    protected void collectLabels(Categories categories, Set<String> keys) {
        for (Category category : categories.list()) {
            for (Label label : category.getLabels().list()) {
                if (keys.contains(label.getKey())) {
                    keyLabelsCache.computeIfAbsent(label.getKey(), f -> new ArrayList<>()).add(label);
                }
            }
            collectLabels(category.getCategories(), keys);
        }
    }
}
