package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.QueryDomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRepositoryImpl extends QueryDomainRepository<User, Users<User>, UserPO> implements UserRepository {

    private final Map<String, UserPO> map = new HashMap<>();

    public UserRepositoryImpl() {
        for (int i = 0; i < 5; i++) {
            String id = String.valueOf(i);
            map.put(id, new UserPO(id));
        }
    }

    @Override
    public User po2do(UserPO po) {
        return new UserImpl(po.getId());
    }

    @Override
    protected UserPO doGet(String id) {
        return map.get(id);
    }

    @Override
    protected Collection<UserPO> doSelect(Collection<String> ids) {
        return ids.stream().map(UserPO::new).collect(Collectors.toList());
    }

    @Override
    protected UserPO doGet(Conditions conditions) {
        return map.get("0");
    }

    @Override
    protected Collection<UserPO> doSelect(Conditions conditions) {
        return map.values();
    }
}
