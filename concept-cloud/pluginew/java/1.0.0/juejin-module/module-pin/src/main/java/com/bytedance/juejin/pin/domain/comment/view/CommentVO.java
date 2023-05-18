package com.bytedance.juejin.pin.domain.comment.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "评论视图")
public class CommentVO {

    @Schema(description = "评论ID")
    private String id;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "评论用户")
    private CommentUserVO user;

    @Schema(description = "评论回复")
    private List<CommentReplyVO> replies;

    @Schema(description = "评论数量")
    private Long commentCount;

    @Schema(description = "点赞数量")
    private Long likeCount;

    @Schema(description = "评论时间")
    private Long createTime;
}
