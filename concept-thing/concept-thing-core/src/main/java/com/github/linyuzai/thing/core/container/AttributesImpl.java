package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.inner.InnerThingAction;
import com.github.linyuzai.thing.core.action.inner.InnerThingActionInvocation;
import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.event.AttributeAddedEvent;
import com.github.linyuzai.thing.core.event.AttributeRemovedEvent;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class AttributesImpl extends AbstractAttributes {

    private final Map<String, Attribute> attributes;

    @Override
    public Attribute get(String id) {
        return attributes.get(id);
    }

    @Override
    public List<Attribute> list() {
        return Collections.unmodifiableList(new ArrayList<>(attributes.values()));
    }

    @Override
    public ThingAction add(Attribute one) {
        return new InnerThingAction(getThing().getContext(), () -> {
            attributes.put(one.getId(), one);
            return new InnerThingActionInvocation(() -> new AttributeAddedEvent(one));
        });
    }

    @Override
    public ThingAction remove(String id) {
        return new InnerThingAction(getThing().getContext(), () -> {
            Attribute remove = attributes.remove(id);
            return new InnerThingActionInvocation(() -> new AttributeRemovedEvent(id, remove));
        });
    }
}
