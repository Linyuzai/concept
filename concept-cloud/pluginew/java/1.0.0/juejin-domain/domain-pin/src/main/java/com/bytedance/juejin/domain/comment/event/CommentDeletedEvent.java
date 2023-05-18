package com.bytedance.juejin.domain.comment.event;

import com.bytedance.juejin.domain.comment.Comment;
import com.bytedance.juejin.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 评论删除事件
 */
@Getter
@RequiredArgsConstructor
public class CommentDeletedEvent {

    /**
     * 删除的评论
     */
    private final Comment comment;

    /**
     * 删除评论的用户
     */
    private final User user;
}
