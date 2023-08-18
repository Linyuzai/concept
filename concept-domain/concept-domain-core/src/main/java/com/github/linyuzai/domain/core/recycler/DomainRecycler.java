package com.github.linyuzai.domain.core.recycler;

import com.github.linyuzai.domain.core.DomainObject;

import java.util.function.Supplier;

public interface DomainRecycler {

    <T extends DomainObject> boolean recycle(Object recycleType, Class<T> domainType, T recyclable);

    <T extends DomainObject> T reuse(Object recycleType, Class<T> domainType, Supplier<T> supplier);
}
