package com.bytedance.juejin.pin.domain.club.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点圈子公告发布命令")
public class ClubAnnouncementPublishCommand {

    @Schema(description = "沸点圈子ID")
    private String id;

    @Schema(description = "沸点圈子公告内容")
    private String announcement;
}
