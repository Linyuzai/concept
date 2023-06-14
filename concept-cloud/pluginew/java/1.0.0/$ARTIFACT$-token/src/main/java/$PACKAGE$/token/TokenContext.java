package $PACKAGE$.token;

import com.github.linyuzai.cloud.web.servlet.WebContextManager;

/**
 * Token 上下文
 */
public class TokenContext {

    public static String getToken() {
        return WebContextManager.get().get(TokenContext.class);
    }

    public static void setToken(String token) {
        WebContextManager.get().put(TokenContext.class, token);
    }
}