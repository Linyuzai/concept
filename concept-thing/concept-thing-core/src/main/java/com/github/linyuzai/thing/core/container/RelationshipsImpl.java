package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Relationship;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RelationshipsImpl extends AbstractRelationships {

    private Thing thing;

    private Map<String, Relationship> map;

    @Override
    protected ThingEvent createAddedEvent(Relationship add) {
        return null;
    }

    @Override
    protected ThingEvent createRemovedEvent(String id, Relationship removed) {
        return null;
    }
}
