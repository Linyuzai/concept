package com.bytedance.juejin.user.domain.user;

import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.user.domain.user.view.UserVO;

/**
 * 用户领域模型转换适配器
 */
public interface UserFacadeAdapter {

    /**
     * 用户领域模型转用户视图
     */
    UserVO do2vo(User user);
}
