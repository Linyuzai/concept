package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractThings extends AbstractContainer<Thing> implements Things {

    private ThingContext context;
}
