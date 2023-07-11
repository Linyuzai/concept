package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;

import java.util.function.Consumer;

public class EmptyConnectionSubscriberFactory extends AbstractScopedFactory<ConnectionSubscriber>
        implements ConnectionSubscriberFactory {

    @Override
    public ConnectionSubscriber create(String scope) {
        return new EmptyConnectionSubscriber();
    }

    public static class EmptyConnectionSubscriber implements ConnectionSubscriber {

        @Override
        public void subscribe(Consumer<Connection> onSuccess, Consumer<Throwable> onError, Runnable onComplete, ConnectionLoadBalanceConcept concept) {
            onComplete.run();
        }
    }
}
