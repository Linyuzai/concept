package $PACKAGE$.module.sample.domain.sample.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "沸点视图")
public class SampleVO {

    @Schema(description = "沸点ID")
    private String id;

    @Schema(description = "沸点内容")
    private String sample;

    @Schema(description = "用户")
    private SampleUserVO user;
}
