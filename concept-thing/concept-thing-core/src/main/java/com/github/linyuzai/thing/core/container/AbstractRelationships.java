package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Thing;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRelationships implements Relationships, Relationships.Modifiable {

    private Thing thing;
}
