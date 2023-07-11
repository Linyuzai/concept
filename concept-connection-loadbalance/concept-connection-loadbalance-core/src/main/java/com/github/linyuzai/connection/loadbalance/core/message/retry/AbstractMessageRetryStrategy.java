package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class AbstractMessageRetryStrategy implements MessageRetryStrategy {

    @Override
    public void retry(Throwable e, Consumer<Consumer<Throwable>> retryable,
                      Consumer<Throwable> onError, ConnectionLoadBalanceConcept concept) {
        int times = getTimes();
        if (times <= 0) {
            onError.accept(e);
            return;
        }
        int current = 1;
        int period = getPeriod(current);
        RetryRunnable retry = new RetryRunnable(current, e, retryable, onError, concept);
        concept.getScheduledExecutor().schedule(retry, period, TimeUnit.MILLISECONDS);
    }

    public abstract int getTimes();

    public abstract int getPeriod(int current);

    @Getter
    @RequiredArgsConstructor
    public class RetryRunnable implements Runnable {

        private final int current;

        private final Throwable error;

        private final Consumer<Consumer<Throwable>> retryable;

        private final Consumer<Throwable> onError;

        private final ConnectionLoadBalanceConcept concept;

        @Override
        public void run() {
            int times = getTimes();
            String currentTimes = current + "/" + times;
            concept.getLogger().info("Start retry and current is " + currentTimes);
            retryable.accept(e -> {
                String retryErrorMessage = "Retry failed " + currentTimes;
                int newCurrent = current + 1;
                int period = getPeriod(newCurrent);
                MessageRetryException retryException =
                        new MessageRetryException(retryErrorMessage, e, error);
                if (current < times) {
                    RetryRunnable retry = new RetryRunnable(newCurrent, retryException, retryable, onError, concept);
                    concept.getScheduledExecutor().schedule(retry, period, TimeUnit.MILLISECONDS);
                } else {
                    onError.accept(retryException);
                }
            });
        }
    }
}
