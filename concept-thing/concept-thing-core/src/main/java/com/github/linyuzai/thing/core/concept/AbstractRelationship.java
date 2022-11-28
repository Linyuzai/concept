package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRelationship implements Relationship, Relationship.Modifiable {

    private String id;

    private String key;

    private String name;

    private Thing major;

    private Thing minor;

    private Relationship opposite;
}
