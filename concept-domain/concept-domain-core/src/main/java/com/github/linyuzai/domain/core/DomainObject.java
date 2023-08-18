package com.github.linyuzai.domain.core;

/**
 * 领域对象
 */
public interface DomainObject extends Identifiable {

    /**
     * 针对延迟加载：立即加载
     */
    default void load() {

    }

    /**
     * 针对延迟加载：立即卸载
     */
    default void unload() {

    }

    /**
     * 针对延迟加载：重新加载
     */
    default void reload() {
        unload();
        load();
    }

    /**
     * 针对代理回收：资源释放
     */
    default void release() {

    }
}
