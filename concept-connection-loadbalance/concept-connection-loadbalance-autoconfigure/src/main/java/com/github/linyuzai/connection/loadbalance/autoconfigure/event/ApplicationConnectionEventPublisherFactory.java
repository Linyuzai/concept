package com.github.linyuzai.connection.loadbalance.autoconfigure.event;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@Getter
@RequiredArgsConstructor
public class ApplicationConnectionEventPublisherFactory implements ConnectionEventPublisherFactory {

    private final ApplicationEventPublisher publisher;

    @Override
    public ConnectionEventPublisher create(String scope) {
        return new ApplicationConnectionEventPublisher(publisher);
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
