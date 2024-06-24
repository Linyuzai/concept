package com.github.linyuzai.connection.loadbalance.core.intercept;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * 连接请求拦截器。
 * <p>
 * Interceptor for connection request.
 *
 * @since 2.7.0
 */
public interface ConnectionRequestInterceptor<Req extends ConnectionRequest, Resp extends ConnectionResponse> extends Scoped {

    /**
     *
     * @param request
     * @param response
     * @return 拦截并返回 true，不拦截 false
     */
    boolean intercept(Req request, Resp response);
}
