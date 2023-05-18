package com.bytedance.juejin.domain.user;

import com.github.linyuzai.domain.core.AbstractDomainBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户实现
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImpl implements User {

    /**
     * 用户ID
     */
    protected String id;

    /**
     * 用户名
     */
    protected String username;

    /**
     * 密码
     */
    protected String password;

    /**
     * 昵称
     */
    protected String nickname;

    /**
     * 头像
     */
    protected String avatar;

    /**
     * 启用
     */
    protected Boolean enabled;

    /**
     * 创建时间
     */
    protected Date createTime;

    @Override
    public void changePassword(String password) {
        this.password = password;
    }

    public static class Builder extends AbstractDomainBuilder<UserImpl> {

        @NotEmpty
        protected String id;

        @NotEmpty
        protected String username;

        @NotNull
        protected String password;

        @NotEmpty
        protected String nickname;

        protected String avatar;

        @NotNull
        protected Boolean enabled;

        @NotNull
        protected Date createTime;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        @Override
        protected void init() {
            if (createTime == null) {
                createTime = new Date();
            }
            if (enabled == null) {
                enabled = true;
            }
        }

        @Override
        protected UserImpl build() {
            return new UserImpl(
                    id,
                    username,
                    password,
                    nickname,
                    avatar,
                    enabled,
                    createTime);
        }
    }
}
