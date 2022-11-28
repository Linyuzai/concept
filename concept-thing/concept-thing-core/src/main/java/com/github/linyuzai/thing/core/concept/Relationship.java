package com.github.linyuzai.thing.core.concept;

public interface Relationship extends IdAndKey {

    String getName();

    Thing getMajor();

    Thing getMinor();

    Relationship getOpposite();

    interface Modifiable extends IdAndKey.Modifiable {

        void setName(String name);

        void setMajor(Thing major);

        void setMinor(Thing minor);

        void setOpposite(Relationship opposite);
    }
}
