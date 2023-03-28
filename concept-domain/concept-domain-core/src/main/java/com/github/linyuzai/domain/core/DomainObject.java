package com.github.linyuzai.domain.core;

/**
 * 领域对象
 */
public interface DomainObject extends Identifiable {

    default void load() {

    }

    default void release() {

    }
}
