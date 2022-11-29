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
        return update(values, UpdateInterceptor.DEFAULT);
    }

    @Override
    public ThingActionChain update(Map<String, Object> values, UpdateInterceptor interceptor) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Attribute attribute = get(key);
            if (attribute == null) {
                interceptor.onAttributeNotFound(this, key, value);
            } else {
                if (interceptor.beforeUpdate(attribute, value)) {
                    attribute.update(value);
                    interceptor.afterUpdate(attribute);
                }
            }
        }
        return thing.actions();
    }
}
