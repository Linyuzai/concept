package com.bytedance.juejin.pin.domain.comment.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点评论删除命令")
public class CommentDeleteCommand {

    @Schema(description = "删除的评论ID")
    private String id;
}
