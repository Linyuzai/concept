package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebError;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebResponse;
import org.springframework.core.Ordered;

import java.util.HashSet;
import java.util.Set;

/**
 * 拦截请求的接口，该接口继承Ordered接口，提供拦截顺序
 */
public interface WebInterceptor extends Ordered {

    Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain);

    default Set<Scope> getScopes() {
        Set<Scope> scopes = new HashSet<>();
        if (getClass().isAnnotationPresent(OnWebRequest.class)) {
            scopes.add(Scope.REQUEST);
        }
        if (getClass().isAnnotationPresent(OnWebResponse.class)) {
            scopes.add(Scope.RESPONSE);
        }
        if (getClass().isAnnotationPresent(OnWebError.class)) {
            scopes.add(Scope.ERROR);
        }
        return scopes;
    }

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    enum Scope {

        REQUEST, RESPONSE, ERROR
    }

    class Order {

        public static final int PREDICATE = Ordered.HIGHEST_PRECEDENCE + 100;

        //Response
        public static final int WEB_RESULT = 1000;
        public static final int STRING_TYPE = 2000;

        //Error
        public static final int LOGGER_ERROR = Ordered.HIGHEST_PRECEDENCE + 1000;
    }
}
