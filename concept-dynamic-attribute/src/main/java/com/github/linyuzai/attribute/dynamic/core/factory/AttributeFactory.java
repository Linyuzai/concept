package com.github.linyuzai.attribute.dynamic.core.factory;

import com.github.linyuzai.attribute.dynamic.core.concept.Attribute;

public interface AttributeFactory {

    Attribute create(Object o);
}
