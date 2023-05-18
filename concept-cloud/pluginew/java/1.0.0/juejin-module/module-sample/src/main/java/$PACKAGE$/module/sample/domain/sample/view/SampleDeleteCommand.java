package $PACKAGE$.module.sample.domain.sample.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "删除命令")
public class SampleDeleteCommand {

    @Schema(description = "ID")
    private String id;
}
