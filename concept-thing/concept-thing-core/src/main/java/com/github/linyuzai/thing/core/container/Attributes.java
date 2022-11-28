package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Attribute;

import java.util.Map;

public interface Attributes extends Container<Attribute>, Container.Modifiable<Attribute> {

    void update(Map<String, Object> values);
}
