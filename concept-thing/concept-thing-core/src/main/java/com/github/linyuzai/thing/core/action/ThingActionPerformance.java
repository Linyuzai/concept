package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.event.ThingEvent;

public interface ThingActionPerformance {

    ThingEvent toEvent();
}