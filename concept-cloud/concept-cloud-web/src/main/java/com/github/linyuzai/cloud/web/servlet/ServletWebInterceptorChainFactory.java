package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;

import java.util.List;

/**
 * webmvc 拦截链工厂
 */
public class ServletWebInterceptorChainFactory implements WebInterceptorChainFactory {

    @Override
    public WebInterceptorChain create(int index, List<WebInterceptor> interceptors) {
        return new ServletWebInterceptorChain(index, interceptors);
    }
}
