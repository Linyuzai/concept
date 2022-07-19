package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

public interface EventOperator {

    EventOperator exchange(EventExchange exchange);

    EventOperator error(EventErrorHandler errorHandler);

    EventOperator encoder(EventEncoder encoder);

    EventOperator decoder(EventDecoder decoder);

    void publish();

    void publish(EventPublisher publisher);

    void subscribe();

    void subscribe(EventSubscriber subscriber);
}
