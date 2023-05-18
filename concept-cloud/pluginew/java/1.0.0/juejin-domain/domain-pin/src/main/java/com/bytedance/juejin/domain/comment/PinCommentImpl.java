package com.bytedance.juejin.domain.comment;

import com.bytedance.juejin.domain.like.CommentLikes;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 沸点的评论实现
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinCommentImpl extends AbstractComment implements PinComment {

    public PinCommentImpl(String id, Pin pin, Pin replyTo, String content, User user, CommentComments comments, CommentLikes likes, Date createTime) {
        super(id, pin, replyTo, content, user, comments, likes, createTime);
    }

    @Override
    public Pin getReplyTo() {
        return (Pin) super.getReplyTo();
    }

    public static class Builder extends AbstractComment.Builder<Pin, PinCommentImpl, Builder> {

        @Override
        protected PinCommentImpl build() {
            return new PinCommentImpl(
                    id,
                    pin,
                    replyTo,
                    content,
                    user,
                    comments,
                    likes,
                    createTime);
        }
    }
}
