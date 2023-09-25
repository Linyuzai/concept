package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 消息重试策略的抽象类。
 * <p>
 * Abstract retry strategy for message sending.
 */
public abstract class AbstractMessageRetryStrategy implements MessageRetryStrategy {

    @Override
    public void retry(Throwable e, Consumer<Consumer<Throwable>> retryable,
                      Consumer<Throwable> onError, ConnectionLoadBalanceConcept concept) {
        //times <= 0 不重试
        //Not retry when times <= 0
        int times = getTimes();
        if (times <= 0) {
            onError.accept(e);
            return;
        }
        //当前重试次数
        //Current retry times
        int current = 1;

        int period = getPeriod(current);
        RetryRunnable retry = new RetryRunnable(current, times, e, retryable, onError, concept);
        concept.getScheduledExecutor().schedule(retry, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 重试总次数。
     * <p>
     * Retry times.
     */
    public abstract int getTimes();

    /**
     * 根据当前重试次数获得重试间隔。
     * <p>
     * Get retry period by current retry times.
     */
    public abstract int getPeriod(int current);

    @Getter
    @RequiredArgsConstructor
    public class RetryRunnable implements Runnable {

        /**
         * 当前重试次数。
         * <p>
         * Current retry times.
         */
        private final int current;

        /**
         * 总重试次数。
         * <p>
         * Retry times.
         */
        private final int times;

        /**
         * 重试原因。
         * 如发送失败的异常或是上一次重试失败的异常。
         * <p>
         * Retry for this error.
         */
        private final Throwable error;

        /**
         * 能够被重试的操作。
         * <p>
         * Operation can be retry.
         */
        private final Consumer<Consumer<Throwable>> retryable;

        /**
         * 结束重试的回调。
         * <p>
         * The callback to end retry.
         */
        private final Consumer<Throwable> onError;

        private final ConnectionLoadBalanceConcept concept;

        @Override
        public void run() {
            String currentTimes = current + "/" + times;
            concept.getLogger().info("Start retry and current is " + currentTimes);
            //进行重试，成功直接结束，失败进入回调
            //Start retry, return if success, consume error if failure
            retryable.accept(e -> {
                String retryErrorMessage = "Retry failed " + currentTimes;
                //重试次数 +1
                //Retry time +1
                int newCurrent = current + 1;
                int period = getPeriod(newCurrent);
                MessageRetryException retryException =
                        new MessageRetryException(retryErrorMessage, e, error);
                //当前重试次数 < 总次数，继续重试
                //Current retry times < total retry times, continue retry
                if (current < times) {
                    RetryRunnable retry = new RetryRunnable(newCurrent, times,
                            retryException, retryable, onError, concept);
                    concept.getScheduledExecutor().schedule(retry, period, TimeUnit.MILLISECONDS);
                } else {
                    //结束重试，回调异常
                    //End retry, callback the error
                    onError.accept(retryException);
                }
            });
        }
    }
}
