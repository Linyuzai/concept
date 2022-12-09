package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Relationship;
import com.github.linyuzai.thing.core.concept.Thing;

public interface Relationships extends Container<Relationship> {

    Thing getThing();

    void setThing(Thing thing);
}
