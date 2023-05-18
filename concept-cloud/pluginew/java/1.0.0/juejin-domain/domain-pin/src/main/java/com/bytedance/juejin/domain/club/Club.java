package com.bytedance.juejin.domain.club;

import com.bytedance.juejin.domain.pin.Pins;
import com.bytedance.juejin.domain.user.Users;
import com.github.linyuzai.domain.core.DomainEntity;

/**
 * 圈子
 */
public interface Club extends DomainEntity {

    /**
     * 圈子名称
     */
    String getName();

    /**
     * 圈子图标
     */
    String getLogo();

    /**
     * 圈子类别
     */
    String getCategory();

    /**
     * 圈子描述
     */
    String getDescription();

    /**
     * 圈子公告
     */
    String getAnnouncement();

    /**
     * 圈子用户
     */
    Users getUsers();

    /**
     * 圈子沸点
     */
    Pins getPins();

    /**
     * 发布公告
     *
     * @param announcement 发布的公告
     */
    void publishAnnouncement(String announcement);
}
