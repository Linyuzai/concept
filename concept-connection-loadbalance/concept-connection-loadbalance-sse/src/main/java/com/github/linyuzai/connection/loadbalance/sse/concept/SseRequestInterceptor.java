package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.intercept.ConnectionRequestInterceptor;

/**
 * SSE 握手拦截器。
 * <p>
 * Interceptor for SSE request.
 *
 * @since 2.7.0
 */
public interface SseRequestInterceptor extends ConnectionRequestInterceptor<SseRequest, SseResponse>, SseScoped {
}
