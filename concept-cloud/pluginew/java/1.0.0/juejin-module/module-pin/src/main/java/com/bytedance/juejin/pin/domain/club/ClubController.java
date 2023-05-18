package com.bytedance.juejin.pin.domain.club;

import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.login.Login;
import com.bytedance.juejin.pin.domain.club.view.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "圈子")
@RestController
@RequestMapping("club")
public class ClubController {

    @Autowired
    protected ClubApplicationService clubApplicationService;

    @Autowired
    protected ClubSearcher clubSearcher;

    @Operation(summary = "新建圈子")
    @PostMapping
    public void create(@RequestBody ClubCreateCommand create, @Login User user) {
        clubApplicationService.create(create, user);
    }

    @Operation(summary = "更新圈子")
    @PutMapping
    public void update(@RequestBody ClubUpdateCommand update, @Login User user) {
        clubApplicationService.update(update, user);
    }

    @Operation(summary = "发布公告圈子")
    @PostMapping("/announcement")
    public void publishAnnouncement(@RequestBody ClubAnnouncementPublishCommand publish, @Login User user) {
        clubApplicationService.publishAnnouncement(publish, user);
    }

    @Operation(summary = "圈子详情")
    @GetMapping("/{id}")
    public ClubVO get(@PathVariable String id) {
        return clubSearcher.get(id);
    }

    @Operation(summary = "圈子列表")
    @GetMapping("/list")
    public List<ClubVO> list(ClubQuery query) {
        return clubSearcher.list(query);
    }
}
