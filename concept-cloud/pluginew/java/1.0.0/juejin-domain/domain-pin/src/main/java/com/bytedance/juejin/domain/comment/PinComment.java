package com.bytedance.juejin.domain.comment;

import com.bytedance.juejin.domain.pin.Pin;

/**
 * 沸点的评论
 */
public interface PinComment extends Comment {

    /**
     * 被评论的沸点
     */
    @Override
    Pin getReplyTo();
}
