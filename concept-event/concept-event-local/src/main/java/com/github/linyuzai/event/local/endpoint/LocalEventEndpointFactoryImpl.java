package com.github.linyuzai.event.local.endpoint;

import com.github.linyuzai.event.local.engine.LocalEventEngine;
import com.github.linyuzai.event.local.properties.LocalEventProperties;

/**
 * 本地事件端点工厂实现
 */
public class LocalEventEndpointFactoryImpl implements LocalEventEndpointFactory {

    @Override
    public LocalEventEndpoint create(String name,
                                     LocalEventProperties.LocalProperties properties,
                                     LocalEventEngine engine) {

        LocalEventEndpoint endpoint = new LocalEventEndpoint(name, engine);
        properties.apply(endpoint);
        return endpoint;
    }
}
