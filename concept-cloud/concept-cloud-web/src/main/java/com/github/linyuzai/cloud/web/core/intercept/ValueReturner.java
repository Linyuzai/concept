package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;

/**
 * 值返回器
 * <p>
 * 主要用于兼容 reactive 场景下返回 Mono
 */
public interface ValueReturner {

    /**
     * 获得拦截链返回值
     *
     * @param context 上下文
     * @return 拦截链返回值
     */
    Object value(WebContext context);
}
