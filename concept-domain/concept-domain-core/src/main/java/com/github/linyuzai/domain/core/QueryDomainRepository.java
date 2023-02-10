package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.page.Pages;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public abstract class QueryDomainRepository<T extends DomainObject, P extends Identifiable> extends AbstractDomainRepository<T, P> {

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
    protected void doDelete(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doDelete(Collection<String> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Collection<P> doSelect(Collection<String> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doDelete(Conditions conditions) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected P doQuery(Conditions conditions) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Long doCount(Conditions conditions) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected List<P> doList(Conditions conditions) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Pages<P> doPage(Conditions conditions, Pages.Args page) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Stream<P> doStream(Conditions conditions) {
        throw new UnsupportedOperationException();
    }
}
