package com.bytedance.juejin.pin.domain.like.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点取消点赞命令")
public class LikeDeleteCommand {

    @Schema(description = "点赞ID")
    private String id;
}
