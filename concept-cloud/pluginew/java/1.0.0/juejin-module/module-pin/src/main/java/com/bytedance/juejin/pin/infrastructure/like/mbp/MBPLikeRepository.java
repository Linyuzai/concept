package com.bytedance.juejin.pin.infrastructure.like.mbp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytedance.juejin.basic.boot.mbp.MBPBaseRepository;
import com.bytedance.juejin.domain.comment.Comment;
import com.bytedance.juejin.domain.like.*;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 基于 MBP 的点赞存储
 */
@Repository
public class MBPLikeRepository extends MBPBaseRepository<Like, Likes<Like>, LikePO> implements LikeRepository {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public LikePO do2po(Like like) {
        LikePO po = new LikePO();
        po.setId(like.getId());
        if (like instanceof PinLike) {
            po.setPinId(like.getLiked().getId());
        }
        if (like instanceof CommentLike) {
            po.setCommentId(like.getLiked().getId());
        }
        po.setUserId(like.getUser().getId());
        po.setCreateTime(like.getCreateTime());
        return po;
    }

    @Override
    public Like po2do(LikePO po) {
        User user = factory.createObject(User.class, po.getUserId());
        if (po.getPinId() != null) {
            Pin pin = factory.createObject(Pin.class, po.getPinId());
            return new PinLikeImpl.Builder()
                    .id(po.getId())
                    .liked(pin)
                    .user(user)
                    .createTime(po.getCreateTime())
                    .build(validator);
        } else if (po.getCommentId() != null) {
            Comment comment = factory.createObject(Comment.class, po.getCommentId());
            return new CommentLikeImpl.Builder()
                    .id(po.getId())
                    .liked(comment)
                    .user(user)
                    .createTime(po.getCreateTime())
                    .build(validator);
        } else {
            throw new RuntimeException("Can not happen");
        }
    }

    @Override
    public BaseMapper<LikePO> getBaseMapper() {
        return likeMapper;
    }
}
