package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.factory.RelationshipFactory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRelationship extends AbstractIdentify implements Relationship {

    private String name;

    private Thing thing;

    private Thing relation;

    private Relationship opposite;

    public Relationship getOpposite() {
        if (opposite == null) {
            RelationshipFactory factory = getContext().get(RelationshipFactory.class);
            opposite = factory.create();
            String suffix = ".OPPOSITE";
            if (name.endsWith(suffix)) {
                opposite.setName(name.substring(0, name.length() - suffix.length()));
            } else {
                opposite.setName(name + suffix);
            }
            opposite.setThing(relation);
            opposite.setRelation(thing);
            opposite.setOpposite(this);
            opposite.setContext(getContext());
        }
        return opposite;
    }
}
