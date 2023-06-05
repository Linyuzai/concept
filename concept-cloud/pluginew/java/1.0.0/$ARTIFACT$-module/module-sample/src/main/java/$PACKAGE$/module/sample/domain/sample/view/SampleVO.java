package $PACKAGE$.module.sample.domain.sample.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "示例视图")
public class SampleVO {

    @Schema(description = "示例ID")
    private String id;

    @Schema(description = "示例内容")
    private String sample;

    @Schema(description = "用户")
    private SampleUserVO user;

    @Schema(description = "用户列表")
    private List<SampleUserVO> users;
}
