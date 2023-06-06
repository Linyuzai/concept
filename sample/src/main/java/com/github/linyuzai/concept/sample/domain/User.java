package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.DomainEntity;
import com.github.linyuzai.domain.core.proxy.DomainProxy;

public interface User extends DomainEntity,
        DomainProxy.ContextAccess, DomainProxy.ConditionsAccess,
        DomainProxy.RepositoryAccess<User>, DomainProxy.ExtraAccess<Object> {

    String getName();

    default void test0() {
        System.out.println("test0");
        System.out.println(getName());
        System.out.println(getContext());
        System.out.println(getConditions());
        System.out.println(getRepository());
        System.out.println(getExtra());
    }
}
