package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PredicateWebInterceptor implements WebInterceptor {

    private Predicate<WebContext> predicate;

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        if (predicate.test(context)) {
            return chain.next(context, returner);
        } else {
            return returner.value(context);
        }
    }

    public PredicateWebInterceptor negate() {
        this.predicate = predicate.negate();
        return this;
    }

    @Override
    public int getOrder() {
        return Orders.PREDICATE;
    }
}
