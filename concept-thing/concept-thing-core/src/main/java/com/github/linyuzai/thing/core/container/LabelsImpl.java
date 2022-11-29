package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionInvocation;
import com.github.linyuzai.thing.core.action.inner.InnerThingAction;
import com.github.linyuzai.thing.core.action.inner.InnerThingActionInvocation;
import com.github.linyuzai.thing.core.concept.Label;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
    public ThingAction add(Label one) {
        return new InnerThingAction(getContext(), () -> {
            labels.put(one.getId(), one);
            return new InnerThingActionInvocation(() -> null);
        });
    }

    @Override
    public ThingAction remove(String id) {
        return null;
    }
}
