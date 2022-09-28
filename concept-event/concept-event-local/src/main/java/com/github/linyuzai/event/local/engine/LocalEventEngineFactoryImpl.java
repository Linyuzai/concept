package com.github.linyuzai.event.local.engine;

import com.github.linyuzai.event.local.properties.LocalEventProperties;

/**
 * 本地事件引擎工厂实现
 */
public class LocalEventEngineFactoryImpl implements LocalEventEngineFactory {

    @Override
    public LocalEventEngine create(LocalEventProperties properties) {
        LocalEventEngine engine = new LocalEventEngine();
        properties.apply(engine);
        return engine;
    }
}
