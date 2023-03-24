package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.page.Pages;

import java.util.Collection;

public abstract class QueryDomainRepository<T extends DomainObject, C extends DomainCollection<T>, P extends Identifiable> extends AbstractDomainRepository<T, C, P> {

    @Override
    public P do2po(T object) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doCreate(P po) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doCreate(Collection<? extends P> pos) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doUpdate(P po) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doUpdate(Collection<? extends P> pos) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doDelete(P po) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doDelete(Collection<? extends P> pos) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doDelete(Conditions conditions) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Long doCount(Conditions conditions) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Pages<P> doPage(Conditions conditions, Pages.Args page) {
        throw new UnsupportedOperationException();
    }
}
