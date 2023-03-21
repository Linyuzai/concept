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

    /**
     * 拦截
     *
     * @param context  上下文
     * @param returner 值返回器
     * @param chain    拦截链
     * @return 调用值返回器返回的值
     */
    Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain);

    /**
     * 获得拦截作用域
     * <p>
     * 默认通过拦截器上是否标记 {@link OnRequest,OnResponse} 来指定拦截作用域
     *
     * @return 拦截作用域
     */
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

    /**
     * 获得排序值
     *
     * @return 排序值
     */
    @Override
    default int getOrder() {
        Order annotation = getClass().getAnnotation(Order.class);
        if (annotation != null) {
            return annotation.value();
        }
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * 拦截器作用域
     * <p>
     * 请求/响应
     */
    enum Scope {

        /**
         * 请求拦截
         */
        REQUEST,

        /**
         * 响应拦截
         */
        RESPONSE
    }

    /**
     * 默认的一些排序值
     */
    class Orders {

        /**
         * 断言
         */
        public static final int PREDICATE = 100;

        //Response
        /**
         * 异常日志
         */
        public static final int LOGGER_ERROR = 0;
        /**
         * 包装响应体
         */
        public static final int WEB_RESULT = 200;
        /**
         * {@link String} 返回类型处理
         */
        public static final int STRING_TYPE = 300;
    }
}
