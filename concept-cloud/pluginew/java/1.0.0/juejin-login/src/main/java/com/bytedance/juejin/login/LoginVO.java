package com.bytedance.juejin.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录视图")
public class LoginVO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "Token")
    private String token;
}
