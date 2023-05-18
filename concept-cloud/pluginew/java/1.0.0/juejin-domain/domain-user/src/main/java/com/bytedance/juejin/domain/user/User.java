package com.bytedance.juejin.domain.user;

import com.github.linyuzai.domain.core.DomainEntity;

import java.util.Date;

/**
 * 用户
 */
public interface User extends DomainEntity {

    /**
     * 用户名
     */
    String getUsername();

    /**
     * 密码
     */
    String getPassword();

    /**
     * 昵称
     */
    String getNickname();

    /**
     * 头像
     */
    String getAvatar();

    /**
     * 启用
     */
    Boolean getEnabled();

    /**
     * 创建时间
     */
    Date getCreateTime();

    /**
     * 修改密码
     */
    void changePassword(String password);
}
