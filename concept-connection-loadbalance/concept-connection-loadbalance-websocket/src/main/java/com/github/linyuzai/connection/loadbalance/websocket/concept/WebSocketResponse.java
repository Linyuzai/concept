package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.intercept.ConnectionResponse;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;

/**
 * WebSocket 连接响应。
 * <p>
 * WebSocket response.
 *
 * @since 2.7.0
 */
public interface WebSocketResponse extends ConnectionResponse, HttpMessage {

    boolean setStatusCode(HttpStatus status);
}
