package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.*;
import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.AttributesImpl;

import java.util.LinkedHashMap;

public class AttributeFactoryImpl implements AttributeFactory {

    @Override
    public Attribute create(Label label, Thing thing, Object value) {
        AttributeImpl attribute = new AttributeImpl();
        attribute.setId(label.getId());
        attribute.setKey(label.getKey());
        attribute.setLabel(label);
        attribute.setThing(thing);
        attribute.setValue(value);
        return attribute;
    }

    @Override
    public Attributes createContainer(Thing thing) {
        AttributesImpl attributes = new AttributesImpl(new LinkedHashMap<>());
        attributes.setThing(thing);
        return attributes;
    }
}
