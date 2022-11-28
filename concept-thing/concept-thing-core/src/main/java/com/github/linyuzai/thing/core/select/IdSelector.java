package com.github.linyuzai.thing.core.select;

import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.concept.IdAndKey;

import java.util.Collection;

public class IdSelector<T extends IdAndKey> implements ThingSelector<T> {

    @Override
    public Collection<T> select(Thing thing) {
        return null;
    }
}
