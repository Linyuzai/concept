package com.bytedance.juejin.pin.domain.comment;

import com.bytedance.juejin.domain.comment.Comment;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.pin.domain.comment.view.CommentCreateCommand;
import com.bytedance.juejin.pin.domain.comment.view.CommentQuery;
import com.bytedance.juejin.pin.domain.comment.view.CommentVO;
import com.github.linyuzai.domain.core.condition.Conditions;

/**
 * 评论领域模型转换适配器
 */
public interface CommentFacadeAdapter {

    /**
     * 创建视图转评论领域模型
     */
    Comment from(CommentCreateCommand create, User user);

    /**
     * 评论领域模型转评论视图
     */
    CommentVO do2vo(Comment comment);

    /**
     * 评论查询转条件
     */
    Conditions toConditions(CommentQuery query);
}
