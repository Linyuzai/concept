package $PACKAGE$.login;

import $PACKAGE$.domain.user.User;
import com.github.linyuzai.cloud.web.servlet.WebContextManager;

/**
 * 登录上下文
 */
public class LoginContext {

    public static User getLogin() {
        return WebContextManager.get().get(LoginContext.class);
    }

    public static void setLogin(User user) {
        WebContextManager.get().put(LoginContext.class, user);
    }
}
