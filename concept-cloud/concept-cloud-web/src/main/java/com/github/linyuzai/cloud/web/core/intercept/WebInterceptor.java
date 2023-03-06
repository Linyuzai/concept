package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnError;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import org.springframework.core.Ordered;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 拦截请求的接口，该接口继承Ordered接口，提供拦截顺序
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
        if (getClass().isAnnotationPresent(OnError.class)) {
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
        public static final int WRAP_RESULT = 1000;
        public static final int STRING_TYPE = 2000;

        //Error
        public static final int LOGGER_ERROR = Ordered.HIGHEST_PRECEDENCE + 1000;
    }
}
