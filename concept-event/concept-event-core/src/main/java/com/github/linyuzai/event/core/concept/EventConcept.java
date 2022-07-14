package com.github.linyuzai.event.core.concept;

public interface EventConcept {

    void publish(Object event);

    void publish(Object event, String... types);

    void publish(Object event, String type, String... names);
}
