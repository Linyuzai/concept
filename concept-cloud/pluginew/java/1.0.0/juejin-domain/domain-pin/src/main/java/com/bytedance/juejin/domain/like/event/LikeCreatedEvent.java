package com.bytedance.juejin.domain.like.event;

import com.bytedance.juejin.domain.like.Like;
import com.bytedance.juejin.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 点赞事件
 */
@Getter
@RequiredArgsConstructor
public class LikeCreatedEvent {

    /**
     * 赞
     */
    private final Like like;

    /**
     * 点赞用户
     */
    private final User user;
}
