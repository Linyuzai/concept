package com.bytedance.juejin.pin.domain.pin.view;

import com.bytedance.juejin.pin.domain.comment.view.CommentVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "沸点视图")
public class PinVO {

    @Schema(description = "沸点ID")
    private String id;

    @Schema(description = "沸点内容")
    private String content;

    @Schema(description = "沸点圈子ID")
    private String clubId;

    @Schema(description = "沸点圈子名称")
    private String clubName;

    @Schema(description = "沸点用户")
    private PinUserVO user;

    @Schema(description = "沸点评论")
    private List<CommentVO> comments;

    @Schema(description = "评论数量")
    private Long commentCount;

    @Schema(description = "点赞数量")
    private Long likeCount;

    @Schema(description = "沸点发布事件")
    private Long createTime;
}
