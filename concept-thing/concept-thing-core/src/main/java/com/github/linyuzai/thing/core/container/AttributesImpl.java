package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.event.AttributeAddedEvent;
import com.github.linyuzai.thing.core.event.AttributeRemovedEvent;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class AttributesImpl extends AbstractAttributes {

    private Thing thing;

    private Map<String, Attribute> map;

    @Override
    protected ThingEvent createAddedEvent(Attribute add) {
        return new AttributeAddedEvent(add);
    }

    @Override
    protected ThingEvent createRemovedEvent(String id, Attribute removed) {
        return new AttributeRemovedEvent(id, removed);
    }
}
