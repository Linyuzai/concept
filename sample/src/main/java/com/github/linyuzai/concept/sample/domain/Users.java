package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.proxy.DomainProxy;

public interface Users<T extends User> extends DomainCollection<T>,
        DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<User>, DomainProxy.ExtraAccess<Object> {

    default void test1() {
        System.out.println("test1");
        System.out.println(this);
        System.out.println(getContext());
        System.out.println(getConditions());
        System.out.println(getRepository());
        System.out.println(getExtra());
    }
}
