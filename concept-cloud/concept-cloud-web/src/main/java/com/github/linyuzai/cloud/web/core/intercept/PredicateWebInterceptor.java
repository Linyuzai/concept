package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.function.Predicate;

/**
 * 断言拦截器
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredicateWebInterceptor implements WebInterceptor {

    /**
     * 断言
     */
    private Predicate<WebContext> predicate;

    /**
     * 作用域
     */
    private Set<Scope> scopes;

    /**
     * 是否使用原始响应体作为返回值
     * <p>
     * 断言拦截顺序靠前，通常配合 negate 打断拦截，导致 WebResult 为 null
     * <p>
     * 设置该值将使用原始的响应体作为返回值
     */
    private boolean useResponseBodyAsWebResult = true;

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        if (predicate.test(context)) {
            //断言通过继续拦截
            onPredicate(true, context);
            return chain.next(context, returner);
        } else {
            //断言不通过直接返回
            onPredicate(false, context);
            return returner.value(context);
        }
    }

    protected void onPredicate(boolean test, WebContext context) {
        //断言不通过 && 响应拦截时 && 使用原始响应体作为返回值
        if (!test && context.get(Scope.class) == Scope.RESPONSE && useResponseBodyAsWebResult) {
            context.put(WebResult.class, context.get(WebContext.Response.BODY));
        }
    }

    /**
     * 取反
     */
    public PredicateWebInterceptor negate() {
        this.predicate = predicate.negate();
        return this;
    }

    @Override
    public Set<Scope> getScopes() {
        if (scopes == null) {
            return WebInterceptor.super.getScopes();
        } else {
            return scopes;
        }
    }

    @Override
    public int getOrder() {
        return Orders.PREDICATE;
    }
}
