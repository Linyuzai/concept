package com.github.linyuzai.thing.core.select;

import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.concept.IdAndKey;

import java.util.Collection;

public interface ThingSelector<T extends IdAndKey> {

    Collection<T> select(Thing thing);
}
