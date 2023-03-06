package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class ServletWebInterceptorChain implements WebInterceptorChain {

    private int index;

    private final List<WebInterceptor> interceptors;

    @Override
    public Object next(WebContext context, ValueReturner returner) {
        if (index < interceptors.size()) {
            WebInterceptor interceptor = interceptors.get(index++);
            return interceptor.intercept(context, returner, this);
        } else {
            return returner.value(context);
        }
    }
}
