package $PACKAGE$.token;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.login.LoginContext;
import com.github.linyuzai.cloud.web.core.concept.Request;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Token 拦截器
 */
@Order(0)
@OnRequest
@Component
public class TokenWebInterceptor implements WebInterceptor {

    public static final String TOKEN_HEADER = "Authorization";

    @Autowired
    private TokenCodec tokenCodec;

    /**
     * 请求 Token 拦截
     */
    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        Request request = context.get(Request.class);
        String token = request.getHeader(TOKEN_HEADER);
        if (token == null) {
            throw new IllegalArgumentException("Token not found");
        }
        User user = tokenCodec.decode(handleToken(token));
        LoginContext.setUser(user);
        LoginContext.setToken(token);
        return chain.next(context, returner);
    }

    private String handleToken(String token) {
        if (token.startsWith("Bearer") || token.startsWith("bearer")) {
            return token.substring(6).trim();
        }
        return token.trim();
    }
}
