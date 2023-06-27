package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendErrorEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

public abstract class AbstractMessageRetryStrategy implements MessageRetryStrategy {

    @Override
    public void retry(Runnable retryable, MessageSendErrorEvent event, ConnectionLoadBalanceConcept concept) {
        int times = getTimes();
        if (times <= 0) {
            concept.getEventPublisher().publish(event);
            return;
        }
        int period = getPeriod(1);
        RetryRunnable retry = new RetryRunnable(1, retryable, event, concept);
        concept.getScheduledExecutor().schedule(retry, period, TimeUnit.MILLISECONDS);
    }

    public abstract int getTimes();

    public abstract int getPeriod(int current);

    @Getter
    @RequiredArgsConstructor
    public class RetryRunnable implements Runnable {

        private final int current;

        private final Runnable runnable;

        private final MessageSendErrorEvent event;

        private final ConnectionLoadBalanceConcept concept;

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Throwable e) {
                int times = getTimes();
                int newCurrent = current + 1;
                int period = getPeriod(newCurrent);
                String retryMessage = "Retry failed " + current + "/" + times;
                MessageSendErrorEvent errorEvent = new MessageSendErrorEvent(
                        event.getConnection(), event.getMessage(),
                        new MessageRetryException(retryMessage, event.getError()));
                if (current < times) {
                    RetryRunnable retry = new RetryRunnable(newCurrent, runnable, errorEvent, concept);
                    concept.getScheduledExecutor().schedule(retry, period, TimeUnit.MILLISECONDS);
                } else {
                    concept.getEventPublisher().publish(errorEvent);
                }
            }
        }
    }
}
