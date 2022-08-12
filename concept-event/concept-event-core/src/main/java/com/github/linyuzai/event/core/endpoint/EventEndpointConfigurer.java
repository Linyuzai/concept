package com.github.linyuzai.event.core.endpoint;

public interface EventEndpointConfigurer<E extends EventEndpoint> {

    void configure(E endpoint);
}
