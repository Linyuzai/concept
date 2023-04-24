package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.concept.Thing;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Attributes extends Container<Attribute> {

    Thing getThing();

    void setThing(Thing thing);

    ThingAction add(Label label);

    ThingAction add(Label label, Function<Attribute, ThingAction> next);

    ThingAction update(Map<String, Object> values);
}
