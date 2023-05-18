package com.bytedance.juejin.rpc.feign.user;

import com.bytedance.juejin.rpc.ConditionsRO;
import com.bytedance.juejin.rpc.Response;
import com.bytedance.juejin.rpc.user.RPCUserFacadeAdapter;
import com.bytedance.juejin.rpc.user.UserRO;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.domain.user.UserRepository;
import com.bytedance.juejin.domain.user.Users;
import com.github.linyuzai.domain.core.QueryDomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 基于 Feign 的用户存储
 */
@Component
public class FeignUserRepository extends QueryDomainRepository<User, Users, UserRO> implements UserRepository {

    @Autowired
    private RPCUserFacadeAdapter rpcUserFacadeAdapter;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public UserRO do2po(User user) {
        return rpcUserFacadeAdapter.do2ro(user);
    }

    @Override
    public User po2do(UserRO ro) {
        return rpcUserFacadeAdapter.ro2do(ro);
    }

    @Override
    protected UserRO doGet(String id) {
        Response<UserRO> response = userFeignClient.get(id);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }

    @Override
    protected Collection<UserRO> doSelect(Collection<String> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected UserRO doGet(Conditions conditions) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Collection<UserRO> doSelect(Conditions conditions) {
        ConditionsRO ro = rpcUserFacadeAdapter.conditions2ro(conditions);
        Response<List<UserRO>> response = userFeignClient.list(ro);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }
}
