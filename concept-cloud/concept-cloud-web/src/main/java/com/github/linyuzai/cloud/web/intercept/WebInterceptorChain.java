package com.github.linyuzai.cloud.web.intercept;

import com.github.linyuzai.cloud.web.context.WebContext;

/**
 * 请求拦截链接口
 */
public interface WebInterceptorChain {

    Object next(WebContext context);
}
