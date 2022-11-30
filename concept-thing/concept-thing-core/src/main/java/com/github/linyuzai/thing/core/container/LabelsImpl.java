package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class LabelsImpl extends AbstractLabels {

    private final Map<String, Label> labels;

    @Override
    public Label get(String id) {
        return labels.get(id);
    }

    @Override
    public List<Label> list() {
        return Collections.unmodifiableList(new ArrayList<>(labels.values()));
    }

    @Override
    protected void onAdd(Label add) {
        labels.put(add.getId(), add);
    }

    @Override
    protected ThingEvent createAddedEvent(Label add) {
        return null;
    }

    @Override
    protected Label onRemove(String id) {
        return labels.remove(id);
    }

    @Override
    protected ThingEvent createRemovedEvent(String id, Label removed) {
        return null;
    }
}
