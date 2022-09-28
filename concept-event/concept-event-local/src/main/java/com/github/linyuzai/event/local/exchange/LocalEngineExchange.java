package com.github.linyuzai.event.local.exchange;

import com.github.linyuzai.event.core.exchange.EngineExchange;
import com.github.linyuzai.event.local.engine.LocalEventEngine;

/**
 * 本地引擎交换机
 * <p>
 * 定位本地引擎下的所有端点
 */
public class LocalEngineExchange extends EngineExchange {

    public LocalEngineExchange() {
        super(LocalEventEngine.NAME);
    }
}
