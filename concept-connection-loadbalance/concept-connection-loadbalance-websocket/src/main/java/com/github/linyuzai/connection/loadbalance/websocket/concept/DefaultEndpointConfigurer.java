package com.github.linyuzai.connection.loadbalance.websocket.concept;

/**
 * 可以对默认的端点进行扩展配置
 * <p>
 * 针对 web 和 webflux 会回调不同的配置对象
 */
public interface DefaultEndpointConfigurer {

    void configure(Object config);
}
