package com.github.linyuzai.domain.core;

public interface DomainProperties {

    <V> V getProperty(Object key);

    void setProperty(Object key, Object value);

    boolean hasProperty(Object key);
}
