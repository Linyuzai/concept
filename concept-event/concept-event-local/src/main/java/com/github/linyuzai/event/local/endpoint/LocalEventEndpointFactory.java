package com.github.linyuzai.event.local.endpoint;

import com.github.linyuzai.event.core.endpoint.EventEndpointFactory;
import com.github.linyuzai.event.local.engine.LocalEventEngine;
import com.github.linyuzai.event.local.properties.LocalEventProperties;

/**
 * 本地事件端点工厂
 */
public interface LocalEventEndpointFactory extends
        EventEndpointFactory<LocalEventProperties.LocalProperties, LocalEventEngine, LocalEventEndpoint> {
}
