package com.bytedance.juejin.pin.domain.club.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点圈子查询条件")
public class ClubQuery {

    @Schema(description = "圈子名称")
    private String name;
}
