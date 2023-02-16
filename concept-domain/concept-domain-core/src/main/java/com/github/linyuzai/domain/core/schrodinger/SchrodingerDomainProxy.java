package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import com.github.linyuzai.domain.core.link.DomainLink;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * 薛定谔模型代理
 *
 * @param <T>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SchrodingerDomainProxy<T extends DomainObject> extends AbstractDomainProxy<T> {

    /**
     * 领域模型 id
     */
    protected String id;

    /**
     * 领域上下文
     */
    protected DomainContext context;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果是 getId 则直接返回
        if ("getId".equals(method.getName())) {
            return id;
        }
        return super.invoke(proxy, method, args);
    }

    /**
     * 获得被代理的对象
     */
    @Override
    public T doGetTarget() {
        DomainRepository<? extends T> repository = context.get(getDomainRepositoryType());
        T domain = repository.get(id);
        if (domain == null) {
            throw new DomainNotFoundException(getDomainType(), id);
        }
        return domain;
    }

    /**
     * 被代理的领域模型的类
     */
    protected abstract Class<? extends T> getDomainType();

    /**
     * 被代理的领域模型的存储
     */
    protected Class<? extends DomainRepository<? extends T>> getDomainRepositoryType() {
        return DomainLink.repository(getDomainType());
    }

    /**
     * 薛定谔模型代理 Builder
     *
     * @param <T>
     * @param <B>
     */
    protected abstract static class Builder<T extends DomainObject, B extends Builder<T, B>> extends AbstractDomainProxy.Builder<T, B> {

        @NotNull
        protected String id;

        @SuppressWarnings("unchecked")
        public B id(String id) {
            this.id = id;
            return (B) this;
        }

        @Override
        protected T doBuild() {
            return proxy(getDomainType(), getDomainProxy());
        }

        /**
         * 领域模型类
         */
        protected abstract Class<? extends T> getDomainType();

        /**
         * 代理接口
         */
        protected abstract DomainProxy<? extends T> getDomainProxy();
    }
}
