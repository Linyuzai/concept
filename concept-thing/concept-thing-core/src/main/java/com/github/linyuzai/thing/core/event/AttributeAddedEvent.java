package com.github.linyuzai.thing.core.event;

import com.github.linyuzai.thing.core.concept.Attribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttributeAddedEvent extends AttributeEvent {

    private final Attribute attribute;
}
