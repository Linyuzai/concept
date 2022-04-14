package com.github.linyuzai.attribute.dynamic.core.concept;

import com.github.linyuzai.attribute.dynamic.core.factory.AttributeFactory;

public interface Attribute {

    String getId();

    String getName();

    <T> T getValue();

    boolean update(Object value);

    AttributeFactory getFactory();
}
