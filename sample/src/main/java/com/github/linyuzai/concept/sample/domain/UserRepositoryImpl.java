package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.QueryDomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class UserRepositoryImpl<P extends UserPO> extends QueryDomainRepository<User, Users, P> implements UserRepository {

    @Override
    public User po2do(UserPO po) {
        return new UserImpl(po.getId());
    }

    @Override
    protected P doGet(String id) {
        return (P) new UserPO(id);
    }

    @Override
    protected Collection<P> doSelect(Collection<String> ids) {
        return (Collection<P>) ids.stream().map(UserPO::new).collect(Collectors.toList());
    }

    @Override
    protected P doGet(Conditions conditions) {
        return null;
    }

    @Override
    protected Collection<P> doSelect(Conditions conditions) {
        return (Collection<P>) Collections.singleton(new UserPO("3"));
    }

    public Class<?> getType() {
       return DomainLink.generic(getClass(), 2);
    }
}
