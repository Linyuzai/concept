package com.github.linyuzai.thing.core.concept;

public interface IdAndKey {

    String getId();

    String getKey();

    //void attach();

    interface Modifiable {

        void setId(String id);

        void setKey(String key);
    }
}
