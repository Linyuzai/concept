package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.container.Attributes;

public interface AttributeFactory {

    Attribute create();

    Attributes createContainer();

}
