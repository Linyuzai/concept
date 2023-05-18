package com.bytedance.juejin.pin.domain.comment.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "评论回复视图")
public class CommentReplyVO {

    @Schema(description = "回复ID")
    private String id;

    @Schema(description = "回复内容")
    private String content;

    @Schema(description = "回复用户")
    private CommentUserVO user;

    @Schema(description = "被回复的用户")
    private CommentUserVO reply;

    @Schema(description = "点赞数量")
    private Long likeCount;

    @Schema(description = "回复时间")
    private Long createTime;
}
