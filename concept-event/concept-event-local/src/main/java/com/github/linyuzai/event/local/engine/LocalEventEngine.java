package com.github.linyuzai.event.local.engine;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.engine.AbstractEventEngine;

/**
 * 本地事件引擎
 */
public class LocalEventEngine extends AbstractEventEngine {

    public static final String NAME = "local";

    public LocalEventEngine() {
        super(NAME);
    }

    /**
     * 获得本地的事件引擎
     */
    public static LocalEventEngine get(EventConcept concept) {
        return (LocalEventEngine) concept.getEngine(NAME);
    }
}
