package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.*;
import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.AttributesImpl;

public class AttributeFactoryImpl implements AttributeFactory {

    @Override
    public Attribute create() {
        return new AttributeImpl();
    }

    @Override
    public Attributes createContainer() {
        return new AttributesImpl();
    }
}
