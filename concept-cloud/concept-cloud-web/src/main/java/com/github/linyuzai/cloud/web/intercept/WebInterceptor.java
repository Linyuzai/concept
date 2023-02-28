package com.github.linyuzai.cloud.web.intercept;

import com.github.linyuzai.cloud.web.context.WebContext;
import com.github.linyuzai.cloud.web.intercept.annotation.OnException;
import com.github.linyuzai.cloud.web.intercept.annotation.OnRequest;
import com.github.linyuzai.cloud.web.intercept.annotation.OnResponse;
import org.springframework.core.Ordered;

import java.util.HashSet;
import java.util.Set;

/**
 * 拦截请求的接口，该接口继承Ordered接口，提供拦截顺序
 */
public interface WebInterceptor extends Ordered {

    Object intercept(WebContext context, WebInterceptorChain chain);

    default Set<Scope> getScopes() {
        Set<Scope> scopes = new HashSet<>();
        if (getClass().isAnnotationPresent(OnRequest.class)) {
            scopes.add(Scope.REQUEST);
        }
        if (getClass().isAnnotationPresent(OnResponse.class)) {
            scopes.add(Scope.RESPONSE);
        }
        if (getClass().isAnnotationPresent(OnException.class)) {
            scopes.add(Scope.EXCEPTION);
        }
        return scopes;
    }

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    enum Scope {

        REQUEST, RESPONSE, EXCEPTION
    }

    class Order {

        public static final int SKIP = Ordered.HIGHEST_PRECEDENCE + 100;

        //Response
        public static final int RESULT_MESSAGE = 0;

        public static final int WRAP_RESULT = 1000;

        public static final int STRING_TYPE = 2000;

        //Error
        public static final int LOGGER_ERROR = Ordered.HIGHEST_PRECEDENCE + 1000;

        public static final int EXCEPTION_RESULT = Ordered.LOWEST_PRECEDENCE - 1000;

    }
}
