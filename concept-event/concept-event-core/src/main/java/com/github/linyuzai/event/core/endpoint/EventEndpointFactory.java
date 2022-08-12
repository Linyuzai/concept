package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.config.EndpointConfig;
import com.github.linyuzai.event.core.engine.EventEngine;

public interface EventEndpointFactory<C extends EndpointConfig, Engine extends EventEngine, Endpoint extends EventEndpoint> {

    Endpoint create(String name, C config, Engine engine);
}
