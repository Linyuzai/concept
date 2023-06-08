package $PACKAGE$.rpc.feign.user;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.UserRepository;
import $PACKAGE$.domain.user.Users;
import $PACKAGE$.rpc.user.RPCUserFacadeAdapter;
import $PACKAGE$.rpc.user.UserRO;
import com.github.linyuzai.domain.core.condition.Conditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供给 Feign 调用的用户接口
 */
@RestController
@RequestMapping("feign/user")
public class FeignUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RPCUserFacadeAdapter rpcUserFacadeAdapter;

    /**
     * 根据用户 id 获得用户信息
     */
    @GetMapping("{id}")
    public UserRO get(@PathVariable String id) {
        User user = userRepository.get(id);
        if (user == null) {
            return null;
        }
        return rpcUserFacadeAdapter.do2ro(user);
    }

    /**
     * 根据用户条件获得用户信息
     * <p>
     * 避免参数过长用 POST
     */
    @PostMapping("conditions")
    public UserRO get(@RequestBody Conditions conditions) {
        User user = userRepository.get(conditions);
        if (user == null) {
            return null;
        }
        return rpcUserFacadeAdapter.do2ro(user);
    }

    /**
     * 根据 id 列表获得用户信息列表
     * <p>
     * 避免参数过长用 POST
     */
    @PostMapping("list/ids")
    public List<UserRO> list(@RequestBody Collection<String> ids) {
        Users users = userRepository.select(ids);
        return users.stream().map(rpcUserFacadeAdapter::do2ro).collect(Collectors.toList());
    }

    /**
     * 根据条件获得用户信息列表
     * <p>
     * 避免参数过长用 POST
     */
    @PostMapping("list/conditions")
    public List<UserRO> conditions(@RequestBody Conditions conditions) {
        Users users = userRepository.select(conditions);
        return users.stream().map(rpcUserFacadeAdapter::do2ro).collect(Collectors.toList());
    }
}
