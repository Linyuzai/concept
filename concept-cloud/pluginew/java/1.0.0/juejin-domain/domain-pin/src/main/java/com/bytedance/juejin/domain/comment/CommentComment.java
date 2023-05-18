package com.bytedance.juejin.domain.comment;

/**
 * 评论的评论
 */
public interface CommentComment extends Comment {

    /**
     * 被评论的评论
     */
    @Override
    Comment getReplyTo();
}
