package com.bytedance.juejin.domain.like;

import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainEntity;
import com.github.linyuzai.domain.core.DomainObject;

import java.util.Date;

/**
 * 点赞
 */
public interface Like extends DomainEntity {

    /**
     * 被点赞的对象，沸点或评论
     */
    DomainObject getLiked();

    /**
     * 点赞的用户
     */
    User getUser();

    /**
     * 点赞时间
     */
    Date getCreateTime();
}
