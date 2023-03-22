package $PACKAGE$.user.domain.user.rpc;

import $PACKAGE$.basic.rpc.api.user.UserRO;
import $PACKAGE$.basic.rpc.api.user.UserApi;
import $PACKAGE$.user.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InnerUserApi implements UserApi {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserRO get(String id) {
        //调用 UserRepository 获得并转为 UserRO
        return null;
    }
}
