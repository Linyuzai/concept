package com.bytedance.juejin.pin.infrastructure.comment.mbp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytedance.juejin.basic.boot.mbp.MBPBaseRepository;
import com.bytedance.juejin.domain.comment.*;
import com.bytedance.juejin.domain.like.CommentLikes;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 基于 MBP 的评论存储实现
 */
@Repository
public class MBPCommentRepository extends MBPBaseRepository<Comment, Comments<Comment>, CommentPO> implements CommentRepository {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public CommentPO do2po(Comment comment) {
        CommentPO po = new CommentPO();
        po.setId(comment.getId());
        po.setPinId(comment.getPin().getId());
        if (comment instanceof CommentComment) {
            po.setCommentId(comment.getReplyTo().getId());
        }
        po.setContent(comment.getContent());
        po.setUserId(comment.getUser().getId());
        po.setCreateTime(comment.getCreateTime());
        return po;
    }

    @Override
    public Comment po2do(CommentPO po) {
        Pin pin = factory.createObject(Pin.class, po.getPinId());
        User user = factory.createObject(User.class, po.getUserId());
        CommentComments comments = factory.createCollection(CommentComments.class, new LambdaConditions()
                .equal(Pin::getId, po.getPinId(), true)
                .equal(Comment::getId, po.getId(), true));
        CommentLikes likes = factory.createCollection(CommentLikes.class,
                new LambdaConditions().equal(Comment::getId, po.getId(), true));
        if (po.getCommentId() == null) {
            return new PinCommentImpl.Builder()
                    .id(po.getId())
                    .pin(pin)
                    .replyTo(pin)
                    .content(po.getContent())
                    .user(user)
                    .comments(comments)
                    .likes(likes)
                    .build(validator);
        } else {
            Comment comment = factory.createObject(Comment.class, po.getCommentId());
            return new CommentCommentImpl.Builder()
                    .id(po.getId())
                    .pin(pin)
                    .replyTo(comment)
                    .content(po.getContent())
                    .user(user)
                    .comments(comments)
                    .likes(likes)
                    .build(validator);
        }
    }

    @Override
    public BaseMapper<CommentPO> getBaseMapper() {
        return commentMapper;
    }
}
