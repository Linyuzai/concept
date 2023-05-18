package com.bytedance.juejin.domain.like;

import com.bytedance.juejin.domain.comment.Comment;

/**
 * 评论的点赞
 */
public interface CommentLike extends Like {

    /**
     * 被点赞的评论
     */
    @Override
    Comment getLiked();
}
