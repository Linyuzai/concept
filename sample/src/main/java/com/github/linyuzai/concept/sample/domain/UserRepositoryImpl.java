package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.QueryDomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;

import java.util.*;
import java.util.stream.Collectors;

public class UserRepositoryImpl extends QueryDomainRepository<User, Users<User>, UserPO> implements UserRepository {

    @Override
    public User po2do(UserPO po) {
        return new UserImpl(po.getId());
    }

    @Override
    protected UserPO doGet(String id) {
        System.out.println("模拟数据库查询 > doGet(id)");
        return new UserPO(id);
    }

    @Override
    protected Collection<UserPO> doSelect(Collection<String> ids) {
        System.out.println("模拟数据库查询 > doSelect(ids)");
        return ids.stream().map(UserPO::new).collect(Collectors.toList());
    }

    @Override
    protected UserPO doGet(Conditions conditions) {
        System.out.println("模拟数据库查询 > doGet(conditions)");
        for (Conditions.Equal equal : conditions.getEquals()) {
            if (equal.getKey().equals("id")) {
                return new UserPO(equal.getValue().toString());
            }
        }
        return null;
    }

    @Override
    protected Collection<UserPO> doSelect(Conditions conditions) {
        System.out.println("模拟数据库查询 > doSelect(conditions)");
        Collection<UserPO> pos = new ArrayList<>();
        for (Conditions.Equal equal : conditions.getEquals()) {
            if (equal.getKey().equals("id")) {
                pos.add(new UserPO(equal.getValue().toString()));
            }
        }
        for (Conditions.In in : conditions.getIns()) {
            if (in.getKey().equals("id")) {
                for (Object value : in.getValues()) {
                    pos.add(new UserPO(value.toString()));
                }
            }
        }
        return pos;
    }
}
