package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.*;
import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.AttributesImpl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class AttributeFactoryImpl implements AttributeFactory {

    @Override
    public Attribute create(Label label, Object value, Collection<Consumer<Thing>> consumers) {
        AttributeImpl attribute = new AttributeImpl();
        attribute.setId(label.getId());
        attribute.setKey(label.getKey());
        attribute.setLabel(label);
        attribute.setValue(value);
        consumers.add(attribute::setThing);
        return attribute;
    }

    @Override
    public Attributes createContainer(Collection<Consumer<Thing>> consumers) {
        AttributesImpl attributes = new AttributesImpl(new LinkedHashMap<>());
        consumers.add(attributes::setThing);
        return attributes;
    }
}
