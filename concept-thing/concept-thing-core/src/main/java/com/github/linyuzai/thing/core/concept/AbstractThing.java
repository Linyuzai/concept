package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractThing implements Thing {

    private String id;

    private String name;

    private Categories categories;

    private Attributes attributes;

    private States states;
}
