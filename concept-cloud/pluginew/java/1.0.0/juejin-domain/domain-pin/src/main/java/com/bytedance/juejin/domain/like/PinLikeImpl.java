package com.bytedance.juejin.domain.like;

import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 被点赞的沸点实现
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinLikeImpl extends AbstractLike implements PinLike {

    public PinLikeImpl(String id, Pin liked, User user, Date createTime) {
        super(id, liked, user, createTime);
    }

    @Override
    public Pin getLiked() {
        return (Pin) super.getLiked();
    }

    public static class Builder extends AbstractLike.Builder<Pin, PinLikeImpl, Builder> {

        @Override
        protected PinLikeImpl build() {
            return new PinLikeImpl(id, liked, user, createTime);
        }
    }
}
