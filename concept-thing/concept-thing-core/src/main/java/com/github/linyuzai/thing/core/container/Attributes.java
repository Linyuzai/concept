package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;

import java.util.Map;

public interface Attributes extends Container<Attribute> {

    Thing getThing();

    void setThing(Thing thing);

    default ThingAction update(Map<String, Object> values) {
        ThingActionChain chain = getContext().actions();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Attribute attribute = get(key);
            if (attribute == null) {
                continue;
            }
            ThingAction update = attribute.update(value);
            chain.next(update);
        }
        return chain;
    }
}
