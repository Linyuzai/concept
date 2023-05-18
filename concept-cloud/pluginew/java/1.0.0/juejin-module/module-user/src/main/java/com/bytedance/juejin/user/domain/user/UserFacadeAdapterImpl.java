package com.bytedance.juejin.user.domain.user;

import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.user.domain.user.view.UserVO;
import org.springframework.stereotype.Component;

/**
 * 用户领域模型转换适配器实现
 */
@Component
public class UserFacadeAdapterImpl implements UserFacadeAdapter {

    @Override
    public UserVO do2vo(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        return vo;
    }
}
