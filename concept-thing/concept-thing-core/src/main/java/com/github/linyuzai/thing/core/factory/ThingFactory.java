package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.container.Things;

public interface ThingFactory {

    Thing create();

    Things createContainer();
}
