package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.event.AttributeAddedEvent;
import com.github.linyuzai.thing.core.event.AttributeRemovedEvent;
import com.github.linyuzai.thing.core.event.ThingEvent;
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
    protected void onAdd(Attribute add) {
        attributes.put(add.getId(), add);
    }

    @Override
    protected ThingEvent createAddedEvent(Attribute add) {
        return new AttributeAddedEvent(add);
    }

    @Override
    protected Attribute onRemove(String id) {
        return attributes.remove(id);
    }

    @Override
    protected ThingEvent createRemovedEvent(String id, Attribute removed) {
        return new AttributeRemovedEvent(id, removed);
    }
}
