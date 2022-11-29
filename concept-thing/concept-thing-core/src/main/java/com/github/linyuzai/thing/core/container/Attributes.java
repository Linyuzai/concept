package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.action.ThingActionInvocation;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.event.AttributeAddedEvent;
import com.github.linyuzai.thing.core.event.AttributeRemovedEvent;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

public interface Attributes extends Container<Attribute>, Container.Modifiable<Attribute> {

    Thing getThing();

    ThingActionChain update(Map<String, Object> values);

    @Getter
    @RequiredArgsConstructor
    class AddInvocation implements ThingActionInvocation, AttributeAddedEvent {

        private final Attribute attribute;

        @Override
        public ThingEvent toEvent() {
            return this;
        }

        @Override
        public Thing getThing() {
            return attribute.getThing();
        }
    }

    @Getter
    @RequiredArgsConstructor
    class RemoveInvocation implements ThingActionInvocation, AttributeRemovedEvent {

        private final String attributeId;

        private final Attribute attribute;

        @Override
        public ThingEvent toEvent() {
            return this;
        }

        @Override
        public Thing getThing() {
            return attribute.getThing();
        }
    }
}
