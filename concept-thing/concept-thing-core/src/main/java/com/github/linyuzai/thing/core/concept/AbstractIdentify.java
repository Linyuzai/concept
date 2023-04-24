package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractIdentify implements Identify {

    private String id;

    private ThingContext context;
}
