package $PACKAGE$.rpc.feign.user;

import $PACKAGE$.rpc.Response;
import $PACKAGE$.rpc.user.RPCUserFacadeAdapter;
import $PACKAGE$.rpc.user.UserRO;
import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.UserRepository;
import $PACKAGE$.domain.user.Users;
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
    public Collection<User> pos2dos(Collection<? extends UserRO> pos) {
        return rpcUserFacadeAdapter.ros2dos(pos);
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
        Response<List<UserRO>> response = userFeignClient.list(ids);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }

    @Override
    protected UserRO doGet(Conditions conditions) {
        Response<UserRO> response = userFeignClient.get(conditions);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }

    @Override
    protected Collection<UserRO> doSelect(Conditions conditions) {
        Response<List<UserRO>> response = userFeignClient.list(conditions);
        if (response.getResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }
}
