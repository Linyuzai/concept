package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractLabels implements Labels, Labels.Modifiable {

    private ThingContext context;
}
