package com.bytedance.juejin.user.domain.user;

import com.bytedance.juejin.user.domain.user.view.UserVO;

/**
 * 用户搜索
 */
public interface UserSearcher {

    /**
     * 根据用户 id 获得用户视图
     */
    UserVO get(String id);
}
