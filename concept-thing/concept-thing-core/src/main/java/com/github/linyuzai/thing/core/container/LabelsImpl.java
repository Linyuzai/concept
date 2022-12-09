package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LabelsImpl extends AbstractLabels {

    private Category category;

    private Map<String, Label> map;

    @Override
    protected ThingEvent createAddedEvent(Label add) {
        return null;
    }

    @Override
    protected ThingEvent createRemovedEvent(String id, Label removed) {
        return null;
    }
}
