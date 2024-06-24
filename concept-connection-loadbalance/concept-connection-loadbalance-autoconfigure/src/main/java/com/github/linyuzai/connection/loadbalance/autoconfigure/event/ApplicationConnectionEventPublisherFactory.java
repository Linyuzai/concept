package com.github.linyuzai.connection.loadbalance.autoconfigure.event;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisherFactory;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 兼容 Spring Events 的事件发布器工厂。
 * <p>
 * Factory of event publisher support Spring Events.
 */
@Getter
@RequiredArgsConstructor
public class ApplicationConnectionEventPublisherFactory implements ConnectionEventPublisherFactory {

    private final ApplicationEventPublisher publisher;

    @Override
    public ConnectionEventPublisher create(String scope) {
        return new ApplicationConnectionEventPublisher(publisher);
    }

    /**
     * 支持所有连接域。
     * <p>
     * Support all connection scope.
     *
     * @see Scoped
     */
    @Override
    public boolean support(String scope) {
        return true;
    }
}
