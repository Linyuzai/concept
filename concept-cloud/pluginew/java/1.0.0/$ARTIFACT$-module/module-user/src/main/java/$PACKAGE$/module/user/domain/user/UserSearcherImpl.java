package $PACKAGE$.module.user.domain.user;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.UserRepository;
import $PACKAGE$.module.user.domain.user.view.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户搜索实现
 */
@Component
public class UserSearcherImpl implements UserSearcher {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFacadeAdapter userFacadeAdapter;

    @Override
    public UserVO get(String id) {
        User user = userRepository.get(id);
        if (user == null) {
            return null;
        }
        return userFacadeAdapter.do2vo(user);
    }
}
