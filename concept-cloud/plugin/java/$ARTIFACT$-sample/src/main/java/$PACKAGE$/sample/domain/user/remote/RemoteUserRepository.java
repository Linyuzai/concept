package $PACKAGE$.sample.domain.user.remote;

import $PACKAGE$.basic.rpc.api.user.UserApi;
import $PACKAGE$.sample.domain.user.User;
import $PACKAGE$.sample.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 使用 {@link UserApi} 来获得用户信息
 */
@Repository
public class RemoteUserRepository implements UserRepository {

    @Autowired
    private UserApi userApi;

    @Override
    public User get(String id) {
        //调用 UserApi 获得并转为 sample 模块的 User
        return null;
    }
}
