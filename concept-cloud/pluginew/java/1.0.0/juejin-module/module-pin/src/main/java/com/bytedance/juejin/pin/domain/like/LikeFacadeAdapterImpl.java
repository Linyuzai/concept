package com.bytedance.juejin.pin.domain.like;

import com.bytedance.juejin.domain.comment.Comment;
import com.bytedance.juejin.domain.like.CommentLikeImpl;
import com.bytedance.juejin.domain.like.Like;
import com.bytedance.juejin.domain.like.PinLikeImpl;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.pin.domain.like.view.LikeCreateCommand;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 点赞领域模型转换适配器实现
 */
@Component
public class LikeFacadeAdapterImpl implements LikeFacadeAdapter {

    @Autowired
    private LikeIdGenerator likeIdGenerator;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public Like from(LikeCreateCommand create, User user) {
        String id = likeIdGenerator.generateId(new Object[]{create, user});
        if (Pin.class.getSimpleName().equalsIgnoreCase(create.getType())) {
            Pin pin = factory.createObject(Pin.class, create.getLikedId());
            return new PinLikeImpl.Builder()
                    .id(id)
                    .liked(pin)
                    .user(user)
                    .build(validator);
        } else if (Comment.class.getSimpleName().equalsIgnoreCase(create.getType())) {
            Comment comment = factory.createObject(Comment.class, create.getLikedId());
            return new CommentLikeImpl.Builder()
                    .id(id)
                    .liked(comment)
                    .user(user)
                    .build(validator);
        } else {
            throw new DomainException("Type not matched");
        }
    }
}
