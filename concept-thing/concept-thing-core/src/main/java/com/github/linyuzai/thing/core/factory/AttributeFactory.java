package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.container.Attributes;

import java.util.Collection;
import java.util.function.Consumer;

public interface AttributeFactory {

    Attribute create(Label label, Object value, Collection<Consumer<Thing>> consumers);

    Attributes createContainer(Collection<Consumer<Thing>> consumers);

}
