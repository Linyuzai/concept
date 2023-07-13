package com.github.linyuzai.connection.loadbalance.core.message.retry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMessageRetryStrategyAdapter extends AbstractMessageRetryStrategyAdapter {

    private int clientMessageRetryTimes;

    private int clientMessageRetryPeriod;

    private int subscribeMessageRetryTimes;

    private int subscribeMessageRetryPeriod;

    private int forwardMessageRetryTimes;

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
