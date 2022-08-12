package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.config.EngineConfig;

public interface EventEngineFactory<C extends EngineConfig, E extends EventEngine> {

    E create(C config);
}
