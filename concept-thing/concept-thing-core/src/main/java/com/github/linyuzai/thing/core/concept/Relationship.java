package com.github.linyuzai.thing.core.concept;

public interface Relationship extends Identify<Relationship> {

    String getName();

    void setName(String name);

    Thing getThing();

    void setThing(Thing major);

    Thing getRelation();

    void setRelation(Thing minor);

    Relationship getOpposite();

    void setOpposite(Relationship opposite);
}
