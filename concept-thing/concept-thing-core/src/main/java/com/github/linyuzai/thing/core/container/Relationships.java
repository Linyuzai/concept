package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Relationship;
import com.github.linyuzai.thing.core.concept.Thing;

import java.util.function.Function;

public interface Relationships extends Container<Relationship> {

    Thing getThing();

    void setThing(Thing thing);

    ThingAction add(Thing relation, String name);

    ThingAction add(Thing relation, String name, Function<Relationship, ThingAction> next);

    ThingAction add(Thing relation, String name, String opposite);

    ThingAction add(Thing relation, String name, String opposite, Function<Relationship, ThingAction> next);
}
