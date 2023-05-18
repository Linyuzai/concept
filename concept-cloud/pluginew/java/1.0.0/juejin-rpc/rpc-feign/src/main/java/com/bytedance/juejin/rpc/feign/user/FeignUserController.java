package com.bytedance.juejin.rpc.feign.user;

import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.domain.user.UserRepository;
import com.bytedance.juejin.domain.user.Users;
import com.bytedance.juejin.rpc.ConditionsRO;
import com.bytedance.juejin.rpc.user.RPCUserFacadeAdapter;
import com.bytedance.juejin.rpc.user.UserRO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供给 Feign 调用的用户接口
 */
@RestController
@RequestMapping("/feign/user")
public class FeignUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RPCUserFacadeAdapter rpcUserFacadeAdapter;

    /**
     * 根据用户 id 获得用户信息
     */
    @GetMapping("/{id}")
    public UserRO get(@PathVariable String id) {
        User user = userRepository.get(id);
        if (user == null) {
            return null;
        }
        return rpcUserFacadeAdapter.do2ro(user);
    }

    /**
     * 根据条件获得用户信息
     * <p>
     * 避免过长用 POST
     */
    @PostMapping("/conditions")
    public List<UserRO> conditions(ConditionsRO ro) {
        Users users = userRepository.select(rpcUserFacadeAdapter.ro2conditions(ro));
        return users.stream().map(rpcUserFacadeAdapter::do2ro).collect(Collectors.toList());
    }
}
