package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.publisher.AbstractEventPublisherGroup;

public class KafkaEventPublisherGroup extends AbstractEventPublisherGroup {

    @Override
    public String getId() {
        return "KAFKA";
    }
}
