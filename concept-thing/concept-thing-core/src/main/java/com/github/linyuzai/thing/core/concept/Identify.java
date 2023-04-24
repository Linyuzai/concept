package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.context.ThingContext;

public interface Identify {

    String getId();

    void setId(String id);

    ThingContext getContext();

    void setContext(ThingContext context);
}
