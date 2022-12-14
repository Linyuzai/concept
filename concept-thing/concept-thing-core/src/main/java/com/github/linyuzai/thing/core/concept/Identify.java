package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.context.ThingContext;

import java.util.function.Function;

public interface Identify<T extends Identify<T>> {

    String getId();

    void setId(String id);

    Function<T, String> getIdProvider();

    void setIdProvider(Function<T, String> idProvider);

    String getKey();

    void setKey(String key);

    Function<T, String> getKeyProvider();

    void setKeyProvider(Function<T, String> keyProvider);

    ThingContext getContext();

    void setContext(ThingContext context);
}
