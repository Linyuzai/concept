package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class AbstractMessageRetryStrategy implements MessageRetryStrategy {

    @Override
    public void retry(Throwable e, Consumer<Consumer<Throwable>> retryable,
                      Consumer<Throwable> error, ConnectionLoadBalanceConcept concept) {
        int times = getTimes();
        if (times <= 0) {
            error.accept(e);
            return;
        }
        int period = getPeriod(1);
        RetryRunnable retry = new RetryRunnable(1, e, retryable, error, concept);
        concept.getScheduledExecutor().schedule(retry, period, TimeUnit.MILLISECONDS);
    }

    public abstract int getTimes();

    public abstract int getPeriod(int current);

    @Getter
    @RequiredArgsConstructor
    public class RetryRunnable implements Runnable {

        private final int current;

        private final Throwable e;

        private final Consumer<Consumer<Throwable>> retryable;

        private final Consumer<Throwable> error;

        private final ConnectionLoadBalanceConcept concept;

        @Override
        public void run() {
            retryable.accept(e -> {
                int times = getTimes();
                int newCurrent = current + 1;
                int period = getPeriod(newCurrent);
                if (current < times) {
                    RetryRunnable retry = new RetryRunnable(newCurrent, e, retryable, error, concept);
                    concept.getScheduledExecutor().schedule(retry, period, TimeUnit.MILLISECONDS);
                } else {
                    String retryErrorMessage = "Retry failed " + current + "/" + times;
                    error.accept(new MessageRetryException(retryErrorMessage, e));
                }
            });
        }
    }
}
