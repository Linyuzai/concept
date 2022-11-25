package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.common.Containable;

public interface Relationship extends Containable {

    Thing major();

    Thing minor();

    Relationship opposite();
}
