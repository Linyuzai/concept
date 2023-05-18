package com.bytedance.juejin.pin.domain.comment.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "评论用户视图")
public class CommentUserVO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户名称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;
}
