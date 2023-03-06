package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;

/**
 * 请求拦截链接口
 */
public interface WebInterceptorChain {

    Object next(WebContext context, ValueReturner returner);
}
