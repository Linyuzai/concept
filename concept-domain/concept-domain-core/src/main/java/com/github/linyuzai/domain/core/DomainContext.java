package com.github.linyuzai.domain.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

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
        } else {
            if (Proxy.isProxyClass(o.getClass())) {
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(o);
                aware(invocationHandler);
            }
        }
    }

    interface Aware {

        void setContext(DomainContext context);
    }
}
