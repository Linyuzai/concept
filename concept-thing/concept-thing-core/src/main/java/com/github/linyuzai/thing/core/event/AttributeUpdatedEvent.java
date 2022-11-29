package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.concept.Attribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttributeUpdatedEvent extends AttributeEvent {

    private final Attribute attribute;

    private final Object oldValue;

    private final Object newValue;
}
