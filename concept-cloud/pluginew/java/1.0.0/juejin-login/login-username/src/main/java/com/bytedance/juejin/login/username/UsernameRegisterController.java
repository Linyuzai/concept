package com.bytedance.juejin.login.username;

import com.bytedance.juejin.domain.user.User;
import com.bytedance.juejin.domain.user.UserIdGenerator;
import com.bytedance.juejin.domain.user.UserImpl;
import com.bytedance.juejin.domain.user.UserRepository;
import com.github.linyuzai.domain.core.DomainValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Tag(name = "注册")
@RestController
@RequestMapping("/register")
public class UsernameRegisterController {

    @Autowired
    private UserIdGenerator userIdGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainValidator validator;

    @Operation(summary = "用户名注册")
    @PostMapping("/username")
    public void usernameRegister(@RequestBody RegisterCommand register) {
        User user = new UserImpl.Builder()
                .id(userIdGenerator.generateId(null))
                .username(register.getUsername())
                .password(register.getPassword())
                .nickname(register.getNickname())
                .avatar(register.getAvatar())
                .createTime(new Date())
                .enabled(true)
                .build(validator);
        userRepository.create(user);
    }

    @Data
    @Schema(description = "用户名注册")
    public static class RegisterCommand {

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "密码")
        private String password;

        @Schema(description = "用户昵称")
        private String nickname;

        @Schema(description = "用户头像")
        private String avatar;
    }
}
