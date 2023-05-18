package $PACKAGE$.module.sample.domain.sample.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "沸点修改命令")
public class SampleUpdateCommand {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "示例")
    private String sample;
}
