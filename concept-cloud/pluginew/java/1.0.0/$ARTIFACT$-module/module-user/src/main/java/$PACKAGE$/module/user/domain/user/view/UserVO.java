package $PACKAGE$.module.user.domain.user.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户视图")
public class UserVO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户名称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;
}
