package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationshipImpl extends AbstractRelationship {

    private String id;

    private String key;

    private String name;

    private Thing thing;

    private Thing relation;

    private Relationship opposite;
}
