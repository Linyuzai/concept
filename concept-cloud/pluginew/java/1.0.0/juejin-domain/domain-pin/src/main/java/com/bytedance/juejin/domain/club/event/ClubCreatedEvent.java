package com.bytedance.juejin.domain.club.event;

import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 沸点圈子创建事件
 */
@Getter
@RequiredArgsConstructor
public class ClubCreatedEvent {

    /**
     * 创建的圈子
     */
    private final Club club;

    /**
     * 创建的用户
     */
    private final User user;
}
