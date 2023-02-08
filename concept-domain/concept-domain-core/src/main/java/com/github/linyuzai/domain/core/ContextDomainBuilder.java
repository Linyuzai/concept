package com.github.linyuzai.domain.core;

import javax.validation.constraints.NotNull;

/**
 * 基于 {@link DomainContext} 的 Builder
 *
 * @param <T>
 * @param <B>
 */
@SuppressWarnings("unchecked")
public abstract class ContextDomainBuilder<T extends DomainObject, B> extends AbstractDomainBuilder<T, B> {

    @NotNull
    protected DomainContext context;

    public B context(DomainContext context) {
        this.context = context;
        return (B) this;
    }
}
