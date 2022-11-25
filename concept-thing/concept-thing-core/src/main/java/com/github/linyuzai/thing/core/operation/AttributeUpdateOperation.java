package com.github.linyuzai.thing.core.operation;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.State;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttributeUpdateOperation extends AbstractOperation {

    private final Attribute attribute;

    private final Object oldValue;

    private final Object newValue;

    @Override
    public Operation merge(Operation operation) {
        return new ComposeOperation().merge(operation);
    }
}
