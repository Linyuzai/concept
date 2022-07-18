package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

public interface EventBuilder {

    EventBuilder exchange(EventExchange exchange);

    EventBuilder error(EventErrorHandler errorHandler);

    void publish();

    void publish(EventPublisher publisher);

    void subscribe();

    void subscribe(EventSubscriber subscriber);
}
