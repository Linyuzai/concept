package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.container.Attributes;

public interface AttributeFactory {

    Attribute create(Label label, Thing thing, Object value);

    Attributes createContainer(Thing thing);

}
