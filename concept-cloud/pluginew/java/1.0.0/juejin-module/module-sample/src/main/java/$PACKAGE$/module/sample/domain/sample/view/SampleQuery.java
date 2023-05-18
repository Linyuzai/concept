package $PACKAGE$.module.sample.domain.sample.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询命令")
public class SampleQuery {

    @Schema(description = "示例")
    private String sample;
}
