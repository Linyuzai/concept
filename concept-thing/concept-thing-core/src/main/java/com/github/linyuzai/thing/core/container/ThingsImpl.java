package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ThingsImpl extends AbstractThings {

    private ThingContext context;

    private Map<String, Thing> map;

    @Override
    protected ThingEvent createAddedEvent(Thing add) {
        return null;
    }

    @Override
    protected ThingEvent createRemovedEvent(String id, Thing removed) {
        return null;
    }
}
