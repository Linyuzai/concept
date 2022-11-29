package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.action.ThingActionInvocation;
import com.github.linyuzai.thing.core.event.AttributeUpdatedEvent;
import com.github.linyuzai.thing.core.event.ThingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface Attribute extends IdAndKey {

    Label getLabel();

    Thing getThing();

    <T> T getValue();

    ThingActionChain update(Object value);

    interface Modifiable extends IdAndKey.Modifiable {

        void setLabel(Label label);

        void setThing(Thing thing);

        void setValue(Object value);
    }

    @Getter
    @RequiredArgsConstructor
    class UpdateInvocation implements ThingActionInvocation, AttributeUpdatedEvent {

        private final Attribute attribute;

        private final Object oldValue;

        private final Object newValue;

        @Override
        public ThingEvent toEvent() {
            return this;
        }

        @Override
        public void publish() {
            attribute.getThing().publish(this);
        }
    }
}
