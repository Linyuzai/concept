package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;
import com.github.linyuzai.cloud.web.servlet.ServletWebInterceptorChain;

import java.util.List;

public class ReactiveWebInterceptorChainFactory implements WebInterceptorChainFactory {

    @Override
    public WebInterceptorChain create(int index, List<WebInterceptor> interceptors) {
        return new ReactiveWebInterceptorChain(index, interceptors, this);
    }
}
