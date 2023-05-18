package com.bytedance.juejin.pin.domain.club;

import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.club.ClubRepository;
import com.bytedance.juejin.domain.club.ClubService;
import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.pin.domain.club.view.ClubAnnouncementPublishCommand;
import com.bytedance.juejin.pin.domain.club.view.ClubCreateCommand;
import com.bytedance.juejin.pin.domain.club.view.ClubUpdateCommand;
import com.github.linyuzai.domain.core.exception.DomainNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 圈子应用服务
 */
@Service
public class ClubApplicationService {

    @Autowired
    protected ClubService clubService;

    @Autowired
    protected ClubFacadeAdapter clubFacadeAdapter;

    @Autowired
    protected ClubRepository clubRepository;

    /**
     * 创建圈子
     */
    public void create(ClubCreateCommand create, User user) {
        Club club = clubFacadeAdapter.from(create);
        clubService.create(club, user);
    }

    /**
     * 更新圈子
     */
    public void update(ClubUpdateCommand update, User user) {
        Club oldClub = clubRepository.get(update.getId());
        if (oldClub == null) {
            throw new DomainNotFoundException(Club.class, update.getId());
        }
        Club newClub = clubFacadeAdapter.from(update, oldClub);
        clubService.update(newClub, oldClub, user);
    }

    /**
     * 发布公告
     */
    public void publishAnnouncement(ClubAnnouncementPublishCommand publish, User user) {
        Club club = clubRepository.get(publish.getId());
        if (club == null) {
            throw new DomainNotFoundException(Club.class, publish.getId());
        }
        clubService.publishAnnouncement(club, publish.getAnnouncement(), user);
    }
}
