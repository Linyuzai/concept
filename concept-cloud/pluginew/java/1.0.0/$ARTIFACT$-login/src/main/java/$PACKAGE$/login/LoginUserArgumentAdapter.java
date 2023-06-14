package $PACKAGE$.login;

import $PACKAGE$.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;

import java.util.Objects;

/**
 * 自动填充标记了 {@link Login} 的 {@link User} 参数
 */
@Getter
@RequiredArgsConstructor
public class LoginUserArgumentAdapter implements LoginArgumentAdapter {

    @Override
    public boolean support(MethodParameter parameter) {
        Class<?> type = parameter.getParameter().getType();
        return User.class == type;
    }

    @Override
    public Object adapt(MethodParameter parameter) {
        Login annotation = parameter.getMethodAnnotation(Login.class);
        User user = LoginContext.getLogin();
        if (user == null) {
            if (!Objects.requireNonNull(annotation).required()) {
                return null;
            }
            throw new IllegalStateException("login.not-login");
        }
        return user;
    }
}
