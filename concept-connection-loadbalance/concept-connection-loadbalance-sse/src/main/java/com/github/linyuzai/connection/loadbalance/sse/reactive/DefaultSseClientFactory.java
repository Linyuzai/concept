package com.github.linyuzai.connection.loadbalance.sse.reactive;

import org.springframework.web.reactive.function.client.WebClient;

public class DefaultSseClientFactory implements ReactiveSseClientFactory {

    @Override
    public WebClient create() {
        return WebClient.create();
    }
}
