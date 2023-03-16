package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.function.Predicate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredicateWebInterceptor implements WebInterceptor {

    private Predicate<WebContext> predicate;

    private Set<Scope> scopes;

    private boolean useResponseBodyAsWebResult = true;

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        if (predicate.test(context)) {
            onPredicate(true, context);
            return chain.next(context, returner);
        } else {
            onPredicate(false, context);
            return returner.value(context);
        }
    }

    protected void onPredicate(boolean test, WebContext context) {
        if (useResponseBodyAsWebResult && context.get(Scope.class) == Scope.RESPONSE) {
            if (!test) {
                context.put(WebResult.class, context.get(WebContext.Response.BODY));
            }
        }
    }

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
