package com.bytedance.juejin.domain.like.event;

import com.bytedance.juejin.domain.like.Like;
import com.bytedance.juejin.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 取消点赞事件
 */
@Getter
@RequiredArgsConstructor
public class LikeDeletedEvent {

    /**
     * 取消的赞
     */
    private final Like like;

    /**
     * 取消的用户
     */
    private final User user;
}
