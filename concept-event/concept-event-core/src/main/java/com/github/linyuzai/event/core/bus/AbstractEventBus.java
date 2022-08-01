package com.github.linyuzai.event.core.bus;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.exchange.EventExchange;

public class AbstractEventBus implements EventBus {

    private EventConcept concept;

    private EventExchange exchange;

    public void publish(Object event) {
        concept.template().publish(event);
    }
}
