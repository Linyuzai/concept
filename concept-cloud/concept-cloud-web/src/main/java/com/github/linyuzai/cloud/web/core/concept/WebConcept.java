package com.github.linyuzai.cloud.web.core.concept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;

/**
 * Web 功能入口
 */
public interface WebConcept {

    /**
     * 是否启用了拦截
     *
     * @return true 启用，false 未启用
     */
    boolean isInterceptionEnabled();

    /**
     * 是否启用了请求拦截
     *
     * @return true 启用，false 未启用
     */
    boolean isRequestInterceptionEnabled();

    /**
     * 是否启用了响应拦截
     *
     * @return true 启用，false 未启用
     */
    boolean isResponseInterceptionEnabled();

    /**
     * 添加拦截器
     *
     * @param interceptor 添加的拦截器
     */
    void addInterceptor(WebInterceptor interceptor);

    /**
     * 移除拦截器
     *
     * @param interceptor 移除的拦截器
     */
    void removeInterceptor(WebInterceptor interceptor);

    /**
     * 拦截请求
     *
     * @param context  上下文
     * @param returner 值返回器
     * @return 调用值返回器返回的值
     */
    Object interceptRequest(WebContext context, ValueReturner returner);

    /**
     * 拦截响应
     *
     * @param context  上下文
     * @param returner 值返回器
     * @return 调用值返回器返回的值
     */
    Object interceptResponse(WebContext context, ValueReturner returner);
}
