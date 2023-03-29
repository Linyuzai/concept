package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.QueryDomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserRepositoryImpl extends QueryDomainRepository<User, Users, UserPO> implements UserRepository {

    @Override
    public User po2do(UserPO po) {
        System.out.println("po2do: " + po.getId());
        return new UserImpl(po.getId());
    }

    @Override
    protected UserPO doGet(String id) {
        System.out.println("doGet: " + id);
        return new UserPO(id);
    }

    @Override
    protected Collection<UserPO> doSelect(Collection<String> ids) {
        System.out.println("doSelect: " + ids);
        return ids.stream().map(UserPO::new).collect(Collectors.toList());
    }

    @Override
    protected UserPO doGet(Conditions conditions) {
        System.out.println("doGet:" + conditions);
        return new UserPO("doGet");
    }

    @Override
    protected Collection<UserPO> doSelect(Conditions conditions) {
        System.out.println("doSelect:" + conditions);
        return Arrays.asList(new UserPO("doSelect1"),new UserPO("doSelect2"));
    }

    public Class<?> getType() {
        return DomainLink.generic(getClass(), 2);
    }
}
