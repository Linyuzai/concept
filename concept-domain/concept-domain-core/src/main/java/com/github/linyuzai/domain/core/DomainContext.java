package com.github.linyuzai.domain.core;

/**
 * 领域上下文
 */
public interface DomainContext {

    /**
     * 通过类获得实例
     */
    <T> T get(Class<T> type);
}
