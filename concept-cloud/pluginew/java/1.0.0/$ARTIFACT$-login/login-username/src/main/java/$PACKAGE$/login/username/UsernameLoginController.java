package $PACKAGE$.login.username;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.domain.user.UserRepository;
import $PACKAGE$.login.LoginAuthorization;
import $PACKAGE$.login.LoginAuthorizer;
import com.github.linyuzai.domain.core.condition.LambdaConditions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登录")
@RestController
@RequestMapping("/login")
public class UsernameLoginController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected LoginAuthorizer loginAuthorizer;

    @Operation(summary = "用户名登录")
    @PostMapping("/username")
    public LoginAuthorization usernameLogin(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.get(new LambdaConditions().equal(User::getUsername, username));
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("login.username-or-password.error");
        }
        if (!user.getEnabled()) {
            throw new IllegalStateException("login.account.disabled");
        }
        return loginAuthorizer.authorize(user);
    }

    //eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2ODIyMTQ4OTEsImlkIjoiYWRtaW4ifQ.OuHgScoymOvaKZf3sINUc8Xeq3XVB-OQ8vCr94bAjfQ
}
