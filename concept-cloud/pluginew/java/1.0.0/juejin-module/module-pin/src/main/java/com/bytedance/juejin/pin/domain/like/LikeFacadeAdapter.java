package com.bytedance.juejin.pin.domain.like;

import com.bytedance.juejin.domain.like.Like;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.pin.domain.like.view.LikeCreateCommand;

/**
 * 点赞领域模型转换适配器
 */
public interface LikeFacadeAdapter {

    /**
     * 创建视图转点赞领域模型
     */
    Like from(LikeCreateCommand create, User user);
}
