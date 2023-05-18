package com.bytedance.juejin.pin.domain.pin.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点删除命令")
public class PinDeleteCommand {

    @Schema(description = "沸点ID")
    private String id;
}
