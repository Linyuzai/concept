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
     * 拦截
     *
     * @return 返回 true 拦截/不继续，返回 false 不拦截/继续
     */
    boolean intercept(Req request, Resp response);
}
