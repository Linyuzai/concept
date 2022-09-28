package com.github.linyuzai.event.local.exchange;

import com.github.linyuzai.event.core.exchange.EndpointExchange;

import java.util.Collection;

/**
 * 本地端点交换机
 * <p>
 * 定位本地引擎下的一个或多个端点
 */
public class LocalEndpointExchange extends EndpointExchange {

    public LocalEndpointExchange(String... endpoints) {
        super(new LocalEngineExchange(), endpoints);
    }

    public LocalEndpointExchange(Collection<String> endpoints) {
        super(new LocalEngineExchange(), endpoints);
    }
}
