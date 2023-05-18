package com.bytedance.juejin.pin.domain.club.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "沸点圈子全量视图")
public class ClubFullVO extends ClubVO {

    @Schema(description = "用户数量")
    private Long userCount;

    @Schema(description = "沸点数量")
    private Long pinCount;
}
