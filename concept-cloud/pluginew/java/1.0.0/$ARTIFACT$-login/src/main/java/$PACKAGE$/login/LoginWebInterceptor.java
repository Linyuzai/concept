package $PACKAGE$.login;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.token.TokenWebInterceptor;
import com.github.linyuzai.cloud.web.core.concept.Request;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(TokenWebInterceptor.ORDER + 1)
@OnRequest
@Component
public class LoginWebInterceptor implements WebInterceptor {

    /**
     * 设置登录用户上下文
     */
    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        Request request = context.get(Request.class);
        String token = request.getHeader(TokenWebInterceptor.TOKEN_HEADER);
        User user = context.get(User.class);
        LoginContext.setToken(token);
        LoginContext.setUser(user);
        return chain.next(context, returner);
    }
}
