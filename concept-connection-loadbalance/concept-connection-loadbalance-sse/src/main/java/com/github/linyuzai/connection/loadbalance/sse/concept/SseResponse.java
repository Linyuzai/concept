package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.intercept.ConnectionResponse;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;

/**
 * SSE 连接响应。
 * <p>
 * SSE response.
 *
 * @since 2.7.0
 */
public interface SseResponse extends ConnectionResponse, HttpMessage {

    boolean setStatusCode(HttpStatus status);
}
