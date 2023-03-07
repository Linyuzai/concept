package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebError;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.HashSet;
import java.util.Set;

/**
 * 拦截请求的接口，该接口继承 {@link Ordered} 接口，提供拦截顺序
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
        Order annotation = getClass().getAnnotation(Order.class);
        if (annotation != null) {
            return annotation.value();
        }
        return Ordered.LOWEST_PRECEDENCE;
    }

    enum Scope {

        REQUEST, RESPONSE, ERROR
    }

    class Orders {

        public static final int PREDICATE = 100;

        //Response
        public static final int WEB_RESULT = 1000;
        public static final int STRING_TYPE = 1100;

        //Error
        public static final int LOGGER_ERROR = 100;
    }
}
