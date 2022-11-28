package com.github.linyuzai.thing.core.concept;

public interface IdAndKey {

    String getId();

    String getKey();

    interface Modifiable {

        void setId(String id);

        void setKey(String key);
    }
}
