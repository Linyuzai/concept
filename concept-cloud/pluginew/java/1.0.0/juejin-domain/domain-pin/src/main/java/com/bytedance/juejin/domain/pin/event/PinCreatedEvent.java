package com.bytedance.juejin.domain.pin.event;

import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 发布沸点事件
 */
@Getter
@RequiredArgsConstructor
public class PinCreatedEvent {

    /**
     * 沸点
     */
    private final Pin pin;

    /**
     * 发布用户
     */
    private final User user;
}
