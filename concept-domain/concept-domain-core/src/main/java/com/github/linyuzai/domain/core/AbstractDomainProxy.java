package com.github.linyuzai.domain.core;

import java.lang.reflect.Proxy;

/**
 * 领域代理抽象类
 *
 * @param <T>
 */
public abstract class AbstractDomainProxy<T extends DomainObject> implements DomainProxy<T> {

    /**
     * 被代理的领域模型
     */
    protected T target;

    @Override
    public T getTarget() {
        if (this.target == null) {
            this.target = doGetTarget();
        }
        return this.target;
    }

    public abstract T doGetTarget();

    /**
     * 领域模型抽象类的 Builder
     *
     * @param <T>
     * @param <B>
     */
    @SuppressWarnings("unchecked")
    protected abstract static class Builder<T extends DomainObject, B extends Builder<T, B>> extends ContextDomainBuilder<T, B> {

        /**
         * 生成代理类
         */
        protected T proxy(Class<? extends T> type, DomainProxy<? extends T> proxy) {
            return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{type}, proxy);
        }
    }
}
