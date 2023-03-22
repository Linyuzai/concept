package $PACKAGE$.basic.rpc.feign.user;

import $PACKAGE$.basic.rpc.feign.FeignResp;
import $PACKAGE$.basic.rpc.api.user.UserRO;
import $PACKAGE$.basic.rpc.api.user.UserApi;
import org.springframework.beans.factory.annotation.Autowired;

public class FeignUserApi implements UserApi {

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public UserRO get(String id) {
        FeignResp<UserRO> response = userFeignClient.get(id);
        if (response.isResult()) {
            return response.getObject();
        }
        throw new RuntimeException(response.getMessage());
    }
}

