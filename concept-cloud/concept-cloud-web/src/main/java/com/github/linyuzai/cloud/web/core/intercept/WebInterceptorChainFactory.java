package com.github.linyuzai.cloud.web.core.intercept;

import java.util.List;

/**
 * 拦截链工厂
 */
public interface WebInterceptorChainFactory {

    /**
     * 创建拦截链
     *
     * @param index        指定拦截器开始下标
     * @param interceptors 拦截器列表
     * @return 链接链
     */
    WebInterceptorChain create(int index, List<WebInterceptor> interceptors);
}
