package $PACKAGE$.basic.rpc.feign.user;

import $PACKAGE$.basic.rpc.api.user.UserRO;
import $PACKAGE$.basic.rpc.feign.FeignResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "$ARTIFACT$-user")
public interface UserFeignClient {

    @GetMapping("/user/{id}")
    FeignResp<UserRO> get(@PathVariable String id);
}
