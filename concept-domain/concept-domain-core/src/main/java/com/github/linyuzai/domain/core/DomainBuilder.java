package com.github.linyuzai.domain.core;

/**
 * 领域模型 Builder
 *
 * @param <T>
 */
public interface DomainBuilder<T extends DomainObject> {

    T build();
}
