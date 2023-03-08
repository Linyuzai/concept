package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
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
