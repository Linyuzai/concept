package com.github.linyuzai.event.core.config;

import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.endpoint.EventEndpointConfigurer;
import com.github.linyuzai.event.core.endpoint.EventEndpointFactory;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.engine.EventEngineConfigurer;
import com.github.linyuzai.event.core.engine.EventEngineFactory;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 事件引擎和事件端点配置流程
 *
 * @param <EngineC>   事件引擎配置类型
 * @param <EndpointC> 事件端点配置类型
 * @param <Engine>    事件引擎类型
 * @param <Endpoint>  事件端点类型
 */
public class EngineEndpointConfiguration<EngineC extends EngineConfig, EndpointC extends EndpointConfig,
        Engine extends EventEngine, Endpoint extends EventEndpoint> {

    /**
     * 实例化并配置事件引擎和事件端点
     */
    public Engine configure(EngineC engineConfig,
                            InheritHandler<EngineC> inheritHandler,
                            EventEngineFactory<EngineC, Engine> engineFactory,
                            EventEndpointFactory<EndpointC, Engine, Endpoint> endpointFactory,
                            List<? extends EventEngineConfigurer<Engine>> engineConfigurers,
                            List<? extends EventEndpointConfigurer<Endpoint>> endpointConfigurers,
                            BiConsumer<String, Endpoint> consumer) {
        //处理配置继承
        inheritHandler.inherit(engineConfig);
        //创建事件引擎
        Engine engine = engineFactory.create(engineConfig);
        //过滤未启用的事件端点
        List<Map.Entry<String, ? extends EndpointConfig>> entries =
                engineConfig.getEndpoints()
                        .entrySet()
                        .stream()
                        .filter(it -> it.getValue().isEnabled())
                        .collect(Collectors.toList());
        for (Map.Entry<String, ? extends EndpointConfig> entry : entries) {
            //事件端点名称
            String name = entry.getKey();
            @SuppressWarnings("unchecked")
            EndpointC endpointConfig = (EndpointC) entry.getValue();
            //创建事件端点
            Endpoint endpoint = endpointFactory.create(name, endpointConfig, engine);
            //事件端点扩展配置
            for (EventEndpointConfigurer<Endpoint> configurer : endpointConfigurers) {
                configurer.configure(endpoint);
            }
            //事件引擎添加事件端点
            engine.addEndpoints(endpoint);
            //事件端点后置处理
            consumer.accept(name, endpoint);
        }
        //事件引擎扩展配置
        for (EventEngineConfigurer<Engine> configurer : engineConfigurers) {
            configurer.configure(engine);
        }
        return engine;
    }
}
