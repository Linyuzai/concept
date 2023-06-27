package com.github.linyuzai.connection.loadbalance.core.executor;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

public interface ScheduledExecutor {

    void schedule(Runnable runnable, long delay, TimeUnit unit, ConnectionLoadBalanceConcept concept);

    default void schedule(Runnable runnable, long delay, TimeUnit unit) {
        schedule(runnable, delay, unit, null);
    }

    void scheduleAtFixedRate(Runnable runnable,
                             long initialDelay,
                             long delay,
                             TimeUnit unit,
                             ConnectionLoadBalanceConcept concept);

    default void scheduleAtFixedRate(Runnable runnable,
                                     long initialDelay,
                                     long delay,
                                     TimeUnit unit) {
        scheduleAtFixedRate(runnable, initialDelay, delay, unit, null);
    }

    void scheduleWithFixedDelay(Runnable runnable,
                                long initialDelay,
                                long delay,
                                TimeUnit unit,
                                ConnectionLoadBalanceConcept concept);

    default void scheduleWithFixedDelay(Runnable runnable,
                                        long initialDelay,
                                        long delay,
                                        TimeUnit unit) {
        scheduleWithFixedDelay(runnable, initialDelay, delay, unit, null);
    }

    void shutdown(ConnectionLoadBalanceConcept concept);

    default void shutdown() {
        shutdown(null);
    }

    @Getter
    @RequiredArgsConstructor
    class Delegate implements ScheduledExecutor {

        private final ConnectionLoadBalanceConcept concept;

        private final ScheduledExecutor delegate;

        public static ScheduledExecutor delegate(ConnectionLoadBalanceConcept concept,
                                                 ScheduledExecutor delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public void schedule(Runnable runnable, long delay, TimeUnit unit, ConnectionLoadBalanceConcept concept) {
            delegate.schedule(runnable, delay, unit, concept);
        }

        @Override
        public void schedule(Runnable runnable, long delay, TimeUnit unit) {
            delegate.schedule(runnable, delay, unit, concept);
        }

        @Override
        public void scheduleAtFixedRate(Runnable runnable, long initialDelay, long delay, TimeUnit unit, ConnectionLoadBalanceConcept concept) {
            delegate.scheduleAtFixedRate(runnable, initialDelay, delay, unit, concept);
        }

        @Override
        public void scheduleAtFixedRate(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
            delegate.scheduleAtFixedRate(runnable, initialDelay, delay, unit, concept);
        }

        @Override
        public void scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit, ConnectionLoadBalanceConcept concept) {
            delegate.scheduleWithFixedDelay(runnable, initialDelay, delay, unit, concept);
        }

        @Override
        public void scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
            delegate.scheduleWithFixedDelay(runnable, initialDelay, delay, unit, concept);
        }

        @Override
        public void shutdown(ConnectionLoadBalanceConcept concept) {
            delegate.shutdown(concept);
        }

        @Override
        public void shutdown() {
            delegate.shutdown(concept);
        }
    }
}
