package com.bytedance.juejin.pin.domain.comment.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "评论查询命令")
public class CommentQuery {

    @Schema(description = "沸点ID")
    private String pinId;
}
