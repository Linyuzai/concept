package com.bytedance.juejin.domain.comment;

import com.bytedance.juejin.domain.like.CommentLikes;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainEntity;
import com.github.linyuzai.domain.core.DomainObject;

import java.util.Date;

/**
 * 评论
 */
public interface Comment extends DomainEntity {

    /**
     * 沸点
     */
    Pin getPin();

    /**
     * 被评论的对象，沸点或评论
     */
    DomainObject getReplyTo();

    /**
     * 内容
     */
    String getContent();

    /**
     * 发布评论的用户
     */
    User getUser();

    /**
     * 评论
     */
    CommentComments getComments();

    /**
     * 点赞
     */
    CommentLikes getLikes();

    /**
     * 评论时间
     */
    Date getCreateTime();
}
