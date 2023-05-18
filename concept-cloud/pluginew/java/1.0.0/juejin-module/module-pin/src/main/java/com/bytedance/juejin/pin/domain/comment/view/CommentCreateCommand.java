package com.bytedance.juejin.pin.domain.comment.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点评论创建命令")
public class CommentCreateCommand {

    @Schema(description = "沸点ID")
    private String pinId;

    @Schema(description = "评论ID")
    private String commentId;

    @Schema(description = "沸点评论内容")
    private String content;
}
