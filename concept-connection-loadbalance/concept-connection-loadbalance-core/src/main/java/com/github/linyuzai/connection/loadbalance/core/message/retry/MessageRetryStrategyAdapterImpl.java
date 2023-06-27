package com.github.linyuzai.connection.loadbalance.core.message.retry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRetryStrategyAdapterImpl extends AbstractMessageRetryStrategyAdapter {

    private int clientMessageRetryTimes;

    private int clientMessageRetryPeriod;

    private int subscribeMessageRetryTimes;

    private int subscribeMessageRetryPeriod;

    private int forwardMessageRetryTimes;

    private int forwardMessageRetryPeriod;

    @Override
    public MessageRetryStrategy getClientMessageRetryStrategy() {
        return new MessageRetryStrategyImpl(clientMessageRetryTimes, clientMessageRetryPeriod);
    }

    @Override
    public MessageRetryStrategy getSubscribeMessageRetryStrategy() {
        return new MessageRetryStrategyImpl(subscribeMessageRetryTimes, subscribeMessageRetryPeriod);
    }

    @Override
    public MessageRetryStrategy getForwardMessageRetryStrategy() {
        return new MessageRetryStrategyImpl(forwardMessageRetryTimes, forwardMessageRetryPeriod);
    }
}
