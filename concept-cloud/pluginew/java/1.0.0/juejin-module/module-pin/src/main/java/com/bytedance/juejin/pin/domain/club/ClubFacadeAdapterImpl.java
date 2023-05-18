package com.bytedance.juejin.pin.domain.club;

import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.club.ClubImpl;
import com.bytedance.juejin.domain.pin.Pins;
import com.bytedance.juejin.domain.user.Users;
import com.bytedance.juejin.pin.domain.club.view.*;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 圈子领域模型转换适配器实现
 */
@Component
public class ClubFacadeAdapterImpl implements ClubFacadeAdapter {

    @Autowired
    private ClubIdGenerator clubIdGenerator;

    @Autowired
    private DomainFactory factory;

    @Autowired
    private DomainValidator validator;

    @Override
    public Club from(ClubCreateCommand create) {
        String id = clubIdGenerator.generateId(create);
        return new ClubImpl.Builder()
                .id(id)
                .name(create.getName())
                .logo(create.getLogo())
                .category(create.getCategory())
                .description(create.getDescription())
                .users(factory.createCollection(Users.class, Collections.emptyList()))
                .pins(factory.createCollection(Pins.class, Collections.emptyList()))
                .build(validator);
    }

    @Override
    public Club from(ClubUpdateCommand update, Club old) {
        return new ClubImpl.Builder()
                .id(update.getId())
                .name(update.getName())
                .logo(update.getLogo())
                .category(update.getCategory())
                .description(update.getDescription())
                .announcement(old.getAnnouncement())
                .users(old.getUsers())
                .pins(old.getPins())
                .build(validator);
    }

    @Override
    public ClubVO do2vo(Club club) {
        ClubVO vo = new ClubVO();
        fill(club, vo);
        return vo;
    }

    @Override
    public ClubFullVO do2full(Club club) {
        ClubFullVO vo = new ClubFullVO();
        fill(club, vo);
        vo.setUserCount(club.getUsers().count());
        vo.setPinCount(club.getPins().count());
        return vo;
    }

    private void fill(Club club, ClubVO vo) {
        vo.setId(club.getId());
        vo.setName(club.getName());
        vo.setLogo(club.getLogo());
        vo.setCategory(club.getCategory());
        vo.setDescription(club.getDescription());
        vo.setAnnouncement(club.getAnnouncement());
    }

    @Override
    public Conditions toConditions(ClubQuery query) {
        return new LambdaConditions()
                .like(Club::getName, query.getName());
    }
}
