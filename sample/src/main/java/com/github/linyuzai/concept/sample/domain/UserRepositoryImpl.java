package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.QueryDomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class UserRepositoryImpl extends QueryDomainRepository<User, Users, UserPO> implements UserRepository {

    @Override
    public User po2do(UserPO po) {
        return new UserImpl(po.getId());
    }

    @Override
    protected UserPO doGet(String id) {
        return new UserPO(id);
    }

    @Override
    protected Collection<UserPO> doSelect(Collection<String> ids) {
        return ids.stream().map(UserPO::new).collect(Collectors.toList());
    }

    @Override
    protected UserPO doGet(Conditions conditions) {
        System.out.println("get:" + conditions);
        return new UserPO("3");
    }

    @Override
    protected Collection<UserPO> doSelect(Conditions conditions) {
        System.out.println("select:" + conditions);
        return Collections.singleton(new UserPO("3"));
    }

    public Class<?> getType() {
        return DomainLink.generic(getClass(), 2);
    }
}
