package com.github.linyuzai.domain.core;

/**
 * 领域上下文
 */
public interface DomainContext {

    /**
     * 通过类获得实例
     */
    <T> T get(Class<T> type);

    default void aware(Object o) {
        if (o instanceof Aware) {
            ((Aware) o).setContext(this);
        }
    }

    interface Aware {

        void setContext(DomainContext context);
    }
}
