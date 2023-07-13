package com.github.linyuzai.connection.loadbalance.core.message.retry;

import lombok.Getter;
import lombok.Setter;

/**
 * 简单消息重试策略适配器。
 * <p>
 * Adapter of simple retry strategy for message sending.
 */
@Getter
@Setter
public class SimpleMessageRetryStrategyAdapter extends AbstractMessageRetryStrategyAdapter {

    /**
     * 客户端连接重试总次数。
     * <p>
     * Total times for client.
     */
    private int clientMessageRetryTimes;

    /**
     * 客户端连接重试间隔。
     * <p>
     * Retry period for client.
     */
    private int clientMessageRetryPeriod;

    /**
     * 订阅连接重试总次数。
     * <p>
     * Total times for subscription.
     */
    private int subscribeMessageRetryTimes;

    /**
     * 订阅连接重试间隔。
     * <p>
     * Retry period for subscription.
     */
    private int subscribeMessageRetryPeriod;

    /**
     * 转发连接重试总次数。
     * <p>
     * Total times for forward.
     */
    private int forwardMessageRetryTimes;

    /**
     * 转发连接重试间隔。
     * <p>
     * Retry period for forward.
     */
    private int forwardMessageRetryPeriod;

    @Override
    public MessageRetryStrategy getClientMessageRetryStrategy() {
        return new SimpleMessageRetryStrategy(clientMessageRetryTimes, clientMessageRetryPeriod);
    }

    @Override
    public MessageRetryStrategy getSubscribeMessageRetryStrategy() {
        return new SimpleMessageRetryStrategy(subscribeMessageRetryTimes, subscribeMessageRetryPeriod);
    }

    @Override
    public MessageRetryStrategy getForwardMessageRetryStrategy() {
        return new SimpleMessageRetryStrategy(forwardMessageRetryTimes, forwardMessageRetryPeriod);
    }
}
