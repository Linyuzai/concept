package com.github.linyuzai.thing.core.concept;

public interface Relationship extends IdAndKey {

    String getName();

    Thing getThing();

    Thing getRelation();

    Relationship getOpposite();

    interface Modifiable extends IdAndKey.Modifiable {

        void setName(String name);

        void setThing(Thing major);

        void setRelation(Thing minor);

        void setOpposite(Relationship opposite);
    }
}
