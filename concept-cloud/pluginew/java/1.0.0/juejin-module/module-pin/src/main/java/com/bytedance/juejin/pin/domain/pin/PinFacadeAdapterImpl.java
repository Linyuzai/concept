package com.bytedance.juejin.pin.domain.pin;

import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.comment.PinComments;
import com.bytedance.juejin.domain.like.PinLikes;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.pin.PinImpl;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.pin.domain.comment.CommentFacadeAdapter;
import com.bytedance.juejin.pin.domain.pin.view.PinCreateCommand;
import com.bytedance.juejin.pin.domain.pin.view.PinQuery;
import com.bytedance.juejin.pin.domain.pin.view.PinUserVO;
import com.bytedance.juejin.pin.domain.pin.view.PinVO;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * 沸点领域模型转换适配器实现
 */
@Component
public class PinFacadeAdapterImpl implements PinFacadeAdapter {

    @Autowired
    private PinIdGenerator pinIdGenerator;

    @Autowired
    private CommentFacadeAdapter commentFacadeAdapter;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public Pin from(PinCreateCommand create, User user) {
        String id = pinIdGenerator.generateId(create);
        Club club;
        if (create.getClubId() == null) {
            club = null;
        } else {
            club = factory.createObject(Club.class, create.getClubId());
        }
        PinComments comments = factory.createCollection(PinComments.class, Collections.emptyList());
        PinLikes likes = factory.createCollection(PinLikes.class, Collections.emptyList());
        return new PinImpl.Builder()
                .id(id)
                .club(club)
                .content(create.getContent())
                .user(user)
                .comments(comments)
                .likes(likes)
                .build(validator);
    }

    @Override
    public PinVO do2vo(Pin pin) {
        PinVO vo = new PinVO();
        vo.setId(pin.getId());
        vo.setContent(pin.getContent());
        if (pin.getClub() != null) {
            vo.setClubId(pin.getClub().getId());
            vo.setClubName(pin.getClub().getName());
        }
        vo.setUser(getUser(pin.getUser()));
        vo.setComments(pin.getComments()
                .getNewestList(5)
                .stream()
                .map(commentFacadeAdapter::do2vo)
                .collect(Collectors.toList()));
        vo.setCommentCount(pin.getComments().count());
        vo.setLikeCount(pin.getLikes().count());
        vo.setCreateTime(pin.getCreateTime().getTime());
        return vo;
    }

    @Override
    public Conditions toConditions(PinQuery query) {
        LambdaConditions conditions = new LambdaConditions();
        conditions.equal(User::getId, query.getUserId());
        conditions.orderBy(Pin::getCreateTime, true, false);
        return conditions;
    }

    private PinUserVO getUser(User user) {
        PinUserVO vo = new PinUserVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        return vo;
    }
}
