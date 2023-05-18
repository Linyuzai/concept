package com.bytedance.juejin.domain.comment;

import com.bytedance.juejin.domain.like.CommentLikes;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 评论的评论实现
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCommentImpl extends AbstractComment implements CommentComment {

    public CommentCommentImpl(String id, Pin pin, Comment replyTo, String content, User user, CommentComments comments, CommentLikes likes, Date createTime) {
        super(id, pin, replyTo, content, user, comments, likes, createTime);
    }

    @Override
    public Comment getReplyTo() {
        return (Comment) super.getReplyTo();
    }

    public static class Builder extends AbstractComment.Builder<Comment, CommentCommentImpl, Builder> {

        @Override
        protected CommentCommentImpl build() {
            return new CommentCommentImpl(
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
