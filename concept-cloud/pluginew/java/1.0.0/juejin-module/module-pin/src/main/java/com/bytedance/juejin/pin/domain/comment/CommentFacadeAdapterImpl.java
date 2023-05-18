package com.bytedance.juejin.pin.domain.comment;

import com.bytedance.juejin.domain.comment.*;
import com.bytedance.juejin.domain.like.CommentLikes;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.pin.domain.comment.view.*;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 评论领域模型转换适配器实现
 */
@Component
public class CommentFacadeAdapterImpl implements CommentFacadeAdapter {

    @Autowired
    private CommentIdGenerator commentIdGenerator;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public Comment from(CommentCreateCommand create, User user) {
        String id = commentIdGenerator.generateId(create);
        Pin pin = factory.createObject(Pin.class, create.getPinId());
        CommentComments comments = factory.createCollection(CommentComments.class,
                Collections.emptyList());
        CommentLikes likes = factory.createCollection(CommentLikes.class,
                Collections.emptyList());
        if (create.getCommentId() == null) {
            return new PinCommentImpl.Builder()
                    .id(id)
                    .pin(pin)
                    .replyTo(pin)
                    .content(create.getContent())
                    .user(user)
                    .comments(comments)
                    .likes(likes)
                    .build(validator);
        } else {
            Comment comment = factory.createObject(Comment.class, create.getCommentId());
            return new CommentCommentImpl.Builder()
                    .id(id)
                    .pin(pin)
                    .replyTo(comment)
                    .content(create.getContent())
                    .user(user)
                    .comments(comments)
                    .likes(likes)
                    .build(validator);
        }
    }

    @Override
    public CommentVO do2vo(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setUser(getUser(comment.getUser()));
        vo.setReplies(getCommentReplyList(comment));
        vo.setCommentCount((long) vo.getReplies().size());
        vo.setLikeCount(comment.getLikes().count());
        vo.setCreateTime(comment.getCreateTime().getTime());
        return vo;
    }

    public List<CommentReplyVO> getCommentReplyList(Comment comment) {
        List<CommentReplyVO> list = new ArrayList<>();
        collectCommentReplyList(comment, list);
        list.sort(Comparator.comparingInt(o -> o.getCreateTime().intValue()));
        return list;
    }

    public void collectCommentReplyList(Comment comment, List<CommentReplyVO> list) {
        comment.getComments().list().forEach(it -> {
            CommentReplyVO vo = new CommentReplyVO();
            vo.setId(it.getId());
            vo.setContent(it.getContent());
            vo.setUser(getUser(it.getUser()));
            vo.setReply(getUser(comment.getUser()));
            vo.setLikeCount(it.getLikes().count());
            vo.setCreateTime(it.getCreateTime().getTime());
            list.add(vo);
            collectCommentReplyList(it, list);
        });
    }

    @Override
    public Conditions toConditions(CommentQuery query) {
        LambdaConditions conditions = new LambdaConditions();
        conditions.equal(Pin::getId, query.getPinId(), true);
        conditions.isNull(Comment::getId, true);
        conditions.orderBy(Comment::getCreateTime, true);
        return conditions;
    }

    private CommentUserVO getUser(User user) {
        CommentUserVO vo = new CommentUserVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        return vo;
    }
}
