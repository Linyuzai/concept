package _PACKAGE_.domain._DOMAIN_.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "_DESC_查询条件")
public class _UPPER_Query {

    @Schema(description = "_DESC_名称")
    private String name;
}
