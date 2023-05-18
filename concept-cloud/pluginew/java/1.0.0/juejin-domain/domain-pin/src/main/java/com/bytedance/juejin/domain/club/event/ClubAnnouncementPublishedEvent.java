package com.bytedance.juejin.domain.club.event;

import com.bytedance.juejin.domain.club.Club;
import com.bytedance.juejin.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 沸点圈子公告发布事件
 */
@Getter
@RequiredArgsConstructor
public class ClubAnnouncementPublishedEvent {

    /**
     * 发布公告的圈子
     */
    private final Club club;

    /**
     * 新公告内容
     */
    private final String newAnnouncement;

    /**
     * 旧公告内容
     */
    private final String oldAnnouncement;

    /**
     * 发布公告的用户
     */
    private final User user;
}
