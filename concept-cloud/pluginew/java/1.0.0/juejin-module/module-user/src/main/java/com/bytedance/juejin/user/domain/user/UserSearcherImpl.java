package com.bytedance.juejin.user.domain.user;

import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.domain.user.UserRepository;
import com.bytedance.juejin.user.domain.user.view.UserVO;
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
