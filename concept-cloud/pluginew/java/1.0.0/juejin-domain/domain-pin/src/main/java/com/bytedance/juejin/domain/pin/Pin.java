package com.bytedance.juejin.domain.pin;

import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.comment.PinComments;
import com.bytedance.juejin.domain.like.PinLikes;
import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainEntity;

import java.util.Date;

/**
 * 沸点
 */
public interface Pin extends DomainEntity {

    /**
     * 圈子
     */
    Club getClub();

    /**
     * 内容
     */
    String getContent();

    /**
     * 发布沸点的用户
     */
    User getUser();

    /**
     * 评论
     */
    PinComments getComments();

    /**
     * 点赞
     */
    PinLikes getLikes();

    /**
     * 发布时间
     */
    Date getCreateTime();
}
