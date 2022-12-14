package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractAttributes extends AbstractContainer<Attribute> implements Attributes {

    private Thing thing;
}
