package com.github.linyuzai.download.core.cache;

public interface Cacheable {

    boolean isCacheEnabled();

    String getCachePath();

    default void deleteCache() {

    }
}
