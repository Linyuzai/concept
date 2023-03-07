package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;

import java.util.List;

public class ServletWebInterceptorChainFactory implements WebInterceptorChainFactory {

    @Override
    public WebInterceptorChain create(List<WebInterceptor> interceptors) {
        return new ServletWebInterceptorChain(interceptors);
    }
}
