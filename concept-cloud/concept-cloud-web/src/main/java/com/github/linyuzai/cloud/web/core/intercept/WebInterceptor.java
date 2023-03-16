package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
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
        if (getClass().isAnnotationPresent(OnRequest.class)) {
            scopes.add(Scope.REQUEST);
        }
        if (getClass().isAnnotationPresent(OnResponse.class)) {
            scopes.add(Scope.RESPONSE);
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

        REQUEST, RESPONSE
    }

    class Orders {

        public static final int PREDICATE = 100;

        //Response
        public static final int LOGGER_ERROR = 0;
        public static final int WEB_RESULT = 200;
        public static final int STRING_TYPE = 300;
    }
}
