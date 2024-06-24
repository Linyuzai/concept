package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.intercept.ConnectionResponse;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;

public interface SseResponse extends ConnectionResponse, HttpMessage {

    boolean setStatusCode(HttpStatus status);
}
