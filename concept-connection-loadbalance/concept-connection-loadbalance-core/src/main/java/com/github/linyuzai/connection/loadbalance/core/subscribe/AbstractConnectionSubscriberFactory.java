package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractConnectionSubscriberFactory extends AbstractScopedFactory<ConnectionSubscriber>
        implements ConnectionSubscriberFactory {

    private int index;
}
