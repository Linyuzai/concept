package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public abstract class WebSocketConnectionSubscriberFactory<T extends WebSocketConnection>
        extends AbstractConnectionSubscriberFactory {

    private String protocol;

    public WebSocketConnectionSubscriberFactory() {
        addScopes(WebSocketScoped.NAME);
    }

    @Override
    public ConnectionSubscriber create(String scope) {
        WebSocketConnectionSubscriber<T> subscriber = doCreate(scope);
        if (StringUtils.hasText(protocol)) {
            subscriber.setProtocol(protocol);
        }
        return subscriber;
    }

    public abstract WebSocketConnectionSubscriber<T> doCreate(String scope);
}
