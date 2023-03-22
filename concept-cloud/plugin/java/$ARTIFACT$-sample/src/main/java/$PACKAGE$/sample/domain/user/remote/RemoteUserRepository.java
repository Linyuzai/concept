package $PACKAGE$.sample.domain.user.remote;

import $PACKAGE$.basic.rpc.api.user.UserRO;
import $PACKAGE$.basic.rpc.api.user.UserApi;
import $PACKAGE$.sample.domain.user.User;
import $PACKAGE$.sample.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class RemoteUserRepository implements UserRepository {

    @Autowired
    private UserApi userApi;

    @Override
    protected User get(String id) {
        //调用 UserApi 获得
        return null;
    }
}
