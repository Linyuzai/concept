package com.bytedance.juejin.domain.like;

import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.AbstractDomainBuilder;
import com.github.linyuzai.domain.core.DomainObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 点赞的领域模型实现
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractLike implements Like {

    /**
     * 点赞ID
     */
    protected String id;

    /**
     * 被点赞的对象，沸点或评论
     */
    protected DomainObject liked;

    /**
     * 点赞的用户
     */
    protected User user;

    /**
     * 点赞时间
     */
    protected Date createTime;

    @SuppressWarnings("unchecked")
    public static abstract class Builder<T extends DomainObject, C extends AbstractLike, B extends Builder<T, C, B>>
            extends AbstractDomainBuilder<C> {

        @NotNull
        protected String id;

        @NotNull
        protected T liked;

        @NotNull
        protected User user;

        @NotNull
        protected Date createTime;

        public B id(String id) {
            this.id = id;
            return (B) this;
        }

        public B liked(T liked) {
            this.liked = liked;
            return (B) this;
        }

        public B user(User user) {
            this.user = user;
            return (B) this;
        }

        public B createTime(Date createTime) {
            this.createTime = createTime;
            return (B) this;
        }

        @Override
        protected void init() {
            if (createTime == null) {
                createTime = new Date();
            }
        }
    }
}
