package $PACKAGE$.rpc.feign.user;

import $PACKAGE$.rpc.Response;
import $PACKAGE$.rpc.user.UserRO;
import com.github.linyuzai.domain.core.condition.Conditions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

/**
 * 用户 Feign 客户端
 */
@FeignClient(name = "user")
public interface UserFeignClient {

    /**
     * 根据 id 获得用户信息
     */
    @GetMapping("feign/user/{id}")
    Response<UserRO> get(@PathVariable String id);

    /**
     * 根据条件获得用户信息
     */
    @PostMapping("feign/user/conditions")
    Response<UserRO> get(@RequestBody Conditions conditions);

    /**
     * 根据 id 列表获得用户信息
     */
    @PostMapping("/feign/user/list/ids")
    Response<List<UserRO>> list(@RequestBody Collection<String> ids);

    /**
     * 根据条件获得用户信息列表
     */
    @PostMapping("feign/user/list/conditions")
    Response<List<UserRO>> list(@RequestBody Conditions conditions);
}
