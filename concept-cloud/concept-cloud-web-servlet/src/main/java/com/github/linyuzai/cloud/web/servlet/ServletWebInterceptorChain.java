package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.context.WebContext;
import com.github.linyuzai.cloud.web.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.intercept.WebInterceptorChain;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ServletWebInterceptorChain implements WebInterceptorChain {

    private int index;

    private final List<WebInterceptor> interceptors;

    @Override
    public Object next(WebContext context) {
        if (index < interceptors.size()) {
            WebInterceptor interceptor = interceptors.get(index++);
            return interceptor.intercept(context, this);
        } else {
            return null;
        }
    }
}
