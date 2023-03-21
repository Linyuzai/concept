package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;

/**
 * 拦截链
 */
public interface WebInterceptorChain {

    /**
     * 继续拦截
     *
     * @param context  上下文
     * @param returner 值返回器
     * @return 调用值返回器返回的值
     */
    Object next(WebContext context, ValueReturner returner);
}
