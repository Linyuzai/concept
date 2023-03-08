package com.github.linyuzai.cloud.web.core.intercept;

import java.util.List;

public interface WebInterceptorChainFactory {

    WebInterceptorChain create(int index, List<WebInterceptor> interceptors);
}
