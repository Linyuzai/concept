package com.bytedance.juejin.domain.club;

import com.bytedance.juejin.domain.club.event.ClubAnnouncementPublishedEvent;
import com.bytedance.juejin.domain.club.event.ClubCreatedEvent;
import com.bytedance.juejin.domain.club.event.ClubUpdatedEvent;
import com.bytedance.juejin.domain.user.User;
import com.github.linyuzai.domain.core.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 圈子领域服务
 */
@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private DomainEventPublisher eventPublisher;

    /**
     * 创建圈子
     */
    public void create(Club club, User user) {
        clubRepository.create(club);
        eventPublisher.publish(new ClubCreatedEvent(club, user));
    }

    /**
     * 更新圈子
     */
    public void update(Club newClub, Club oldClub, User user) {
        clubRepository.update(newClub);
        eventPublisher.publish(new ClubUpdatedEvent(newClub, oldClub, user));
    }

    /**
     * 发布公告
     */
    public void publishAnnouncement(Club club, String newAnnouncement, User user) {
        String oldAnnouncement = club.getAnnouncement();
        club.publishAnnouncement(newAnnouncement);
        clubRepository.update(club);
        eventPublisher.publish(new ClubAnnouncementPublishedEvent(club,
                newAnnouncement, oldAnnouncement, user));
    }
}
