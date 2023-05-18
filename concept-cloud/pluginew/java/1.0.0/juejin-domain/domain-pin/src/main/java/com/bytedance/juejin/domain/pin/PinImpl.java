package com.bytedance.juejin.domain.pin;

import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.comment.PinComments;
import com.bytedance.juejin.domain.like.PinLikes;
import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.AbstractDomainBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 沸点
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PinImpl implements Pin {

    /**
     * 沸点ID
     */
    protected String id;

    /**
     * 圈子
     */
    protected Club club;

    /**
     * 内容
     */
    protected String content;

    /**
     * 发布沸点的用户
     */
    protected User user;

    /**
     * 评论
     */
    protected PinComments comments;

    /**
     * 点赞
     */
    protected PinLikes likes;

    /**
     * 发布时间
     */
    protected Date createTime;

    public static class Builder extends AbstractDomainBuilder<PinImpl> {

        @NotEmpty
        protected String id;

        protected Club club;

        @NotEmpty
        protected String content;

        @NotNull
        protected User user;

        @NotNull
        protected PinComments comments;

        @NotNull
        protected PinLikes likes;

        @NotNull
        protected Date createTime;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder club(Club club) {
            this.club = club;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder comments(PinComments comments) {
            this.comments = comments;
            return this;
        }

        public Builder likes(PinLikes likes) {
            this.likes = likes;
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
        }

        @Override
        protected PinImpl build() {
            return new PinImpl(
                    id,
                    club,
                    content,
                    user,
                    comments,
                    likes,
                    createTime);
        }
    }
}
