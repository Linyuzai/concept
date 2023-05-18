package com.bytedance.juejin.domain.pin.event;

import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 删除沸点事件
 */
@Getter
@RequiredArgsConstructor
public class PinDeletedEvent {

    /**
     * 删除的沸点
     */
    private final Pin pin;

    /**
     * 删除的用户
     */
    private final User user;
}
