package com.github.linyuzai.connection.loadbalance.sse.reactive;

import org.springframework.web.reactive.function.client.WebClient;

public interface ReactiveSseClientFactory {

    WebClient create();
}
