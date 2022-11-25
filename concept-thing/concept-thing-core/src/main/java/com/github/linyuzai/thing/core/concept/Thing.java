package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.common.Containable;

public interface Thing extends Containable {

    String name();

    Category category(String id);

    Attribute attribute(String id);

    State state(String id);

    Categories categories();

    Attributes attributes();

    States states();

    Relationships relationships();

    <T> T create(Class<T> target);
}
