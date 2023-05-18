package com.bytedance.juejin.pin.domain.like.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点点赞命令")
public class LikeCreateCommand {

    @Schema(description = "沸点ID/评论ID")
    private String likedId;

    @Schema(description = "沸点/评论")
    private String type;
}
