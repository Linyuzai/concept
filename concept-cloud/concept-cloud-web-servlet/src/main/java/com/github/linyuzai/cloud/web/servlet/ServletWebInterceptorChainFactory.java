package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.intercept.WebInterceptorChain;
import com.github.linyuzai.cloud.web.intercept.WebInterceptorChainFactory;

import java.util.List;

public class ServletWebInterceptorChainFactory implements WebInterceptorChainFactory {

    @Override
    public WebInterceptorChain create(List<WebInterceptor> interceptors) {
        return new ServletWebInterceptorChain(0, interceptors);
    }
}
