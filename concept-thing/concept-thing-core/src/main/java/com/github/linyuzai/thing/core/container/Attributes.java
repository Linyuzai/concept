package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;

import java.util.Map;

public interface Attributes extends Container<Attribute>, Container.Modifiable<Attribute> {

    Thing getThing();

    ThingActionChain update(Map<String, Object> values);

    ThingActionChain update(Map<String, Object> values, UpdateInterceptor interceptor);

    interface UpdateInterceptor {

        UpdateInterceptor DEFAULT = new UpdateInterceptor() {
        };

        default boolean beforeUpdate(Attribute attribute, Object value) {
            return true;
        }

        default void afterUpdate(Attribute attribute) {

        }

        default void onAttributeNotFound(Attributes attributes, String id, Object value) {

        }
    }
}
