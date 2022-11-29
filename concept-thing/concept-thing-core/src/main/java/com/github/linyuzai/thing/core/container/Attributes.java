package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActions;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Attributes extends Container<Attribute> {

    Thing getThing();

    default ThingAction update(Map<String, Object> values) {
        List<ThingAction> actions = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Attribute attribute = get(key);
            if (attribute == null) {
                continue;
            }
            ThingAction update = attribute.update(value);
            actions.add(update);
        }
        return new ThingActions(getThing().getContext(), actions);
    }

    interface Modifiable {

        void setThing(Thing thing);
    }
}
