package com.bytedance.juejin.domain.like;

import com.bytedance.juejin.domain.pin.Pin;

/**
 * 沸点的点赞
 */
public interface PinLike extends Like {

    /**
     * 被点赞的沸点
     */
    @Override
    Pin getLiked();
}
