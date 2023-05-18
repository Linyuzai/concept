package com.bytedance.juejin.pin.domain.pin.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点修改命令")
public class PinUpdateCommand {

    @Schema(description = "沸点ID")
    private String id;

    @Schema(description = "沸点圈子ID")
    private String clubId;
}
