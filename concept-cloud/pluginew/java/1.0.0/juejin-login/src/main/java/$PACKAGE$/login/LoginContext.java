package $PACKAGE$.login;

import $PACKAGE$.domain.user.User;
import com.github.linyuzai.cloud.web.servlet.WebContextManager;

/**
 * 登录上下文
 */
public class LoginContext {

    private static final String USER = LoginContext.class.getName() + "@user";

    private static final String TOKEN = LoginContext.class.getName() + "@token";

    public static User getUser() {
        return WebContextManager.get().get(USER);
    }

    public static void setUser(User user) {
        WebContextManager.get().put(USER, user);
    }

    public static String getToken() {
        return WebContextManager.get().get(TOKEN);
    }

    public static void setToken(String token) {
        WebContextManager.get().put(TOKEN, token);
    }
}
