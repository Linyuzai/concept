package com.bytedance.juejin.pin.infrastructure.pin.mbp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytedance.juejin.basic.boot.mbp.MBPBaseRepository;
import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.comment.Comment;
import com.bytedance.juejin.domain.comment.PinComments;
import com.bytedance.juejin.domain.like.PinLikes;
import com.bytedance.juejin.domain.pin.Pin;
import com.bytedance.juejin.domain.pin.PinImpl;
import com.bytedance.juejin.domain.pin.PinRepository;
import com.bytedance.juejin.domain.pin.Pins;
import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 基于 MBP 的沸点存储实现
 */
@Repository
public class MBPPinRepository extends MBPBaseRepository<Pin, Pins, PinPO> implements PinRepository {

    @Autowired
    private PinMapper pinMapper;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public PinPO do2po(Pin pin) {
        PinPO po = new PinPO();
        po.setId(pin.getId());
        if (pin.getClub() != null) {
            po.setClubId(pin.getClub().getId());
        }
        po.setContent(pin.getContent());
        po.setUserId(pin.getUser().getId());
        po.setCreateTime(pin.getCreateTime());
        return po;
    }

    @Override
    public Pin po2do(PinPO po) {
        Club club;
        String clubId = po.getClubId();
        if (clubId == null) {
            club = null;
        } else {
            club = factory.createObject(Club.class, po.getClubId());
        }
        User user = factory.createObject(User.class, po.getUserId());
        PinComments comments = factory.createCollection(PinComments.class, new LambdaConditions()
                .equal(Pin::getId, po.getId(), true)
                .isNull(Comment::getId, true));
        PinLikes likes = factory.createCollection(PinLikes.class, new LambdaConditions()
                .equal(Pin::getId, po.getId(), true));
        return new PinImpl.Builder()
                .id(po.getId())
                .club(club)
                .content(po.getContent())
                .user(user)
                .comments(comments)
                .likes(likes)
                .createTime(po.getCreateTime())
                .build(validator);
    }

    @Override
    public BaseMapper<PinPO> getBaseMapper() {
        return pinMapper;
    }
}
