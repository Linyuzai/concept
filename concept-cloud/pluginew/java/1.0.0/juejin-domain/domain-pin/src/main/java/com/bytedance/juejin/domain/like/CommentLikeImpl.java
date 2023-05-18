package com.bytedance.juejin.domain.like;

import com.bytedance.juejin.domain.comment.Comment;
import com.bytedance.juejin.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 被点赞的评论实现
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeImpl extends AbstractLike implements CommentLike {

    public CommentLikeImpl(String id, Comment liked, User user, Date createTime) {
        super(id, liked, user, createTime);
    }

    @Override
    public Comment getLiked() {
        return (Comment) super.getLiked();
    }

    public static class Builder extends AbstractLike.Builder<Comment, CommentLikeImpl, Builder> {

        @Override
        protected CommentLikeImpl build() {
            return new CommentLikeImpl(id, liked, user, createTime);
        }
    }
}
