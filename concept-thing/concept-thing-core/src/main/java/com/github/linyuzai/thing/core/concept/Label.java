package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.common.Containable;

public interface Label extends Containable {

    String name();

    Category category();
}
