package $PACKAGE$.module.sample.domain.sample.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建命令")
public class SampleCreateCommand {

    @Schema(description = "示例")
    private String sample;
}
