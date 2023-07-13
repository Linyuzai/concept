package com.github.linyuzai.connection.loadbalance.core.message.retry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 简单的消息重试策略。
 * 可指定次数和固定的间隔。
 * <p>
 * Simple retry strategy for message sending with total times and fixed period.
 */
@Getter
@RequiredArgsConstructor
public class SimpleMessageRetryStrategy extends AbstractMessageRetryStrategy {

    /**
     * 总次数。
     * <p>
     * Total times.
     */
    private final int times;

    /**
     * 重试间隔。
     * <p>
     * Retry period.
     */
    private final int period;

    @Override
    public int getPeriod(int current) {
        return period;
    }
}
