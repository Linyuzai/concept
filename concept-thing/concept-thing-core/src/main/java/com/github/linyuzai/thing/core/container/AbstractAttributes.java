package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class AbstractAttributes implements Attributes {

    private Thing thing;

    @Override
    public ThingActionChain update(Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            update(key, value);
        }
        return thing.actions();
    }

    protected void update(String id, Object value) {
        Attribute attribute = getAttribute(id);
        if (attribute == null) {
            return;
        }
        attribute.update(value);
    }

    protected Attribute getAttribute(String id) {
        return get(id);
    }
}
