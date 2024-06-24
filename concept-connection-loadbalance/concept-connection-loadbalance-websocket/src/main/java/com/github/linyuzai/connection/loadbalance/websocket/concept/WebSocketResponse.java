package com.github.linyuzai.connection.loadbalance.websocket.concept;

import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;

public interface WebSocketResponse extends HttpMessage {

    boolean setStatusCode(HttpStatus status);
}
