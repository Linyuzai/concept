package com.github.linyuzai.connection.loadbalance.core.message.retry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimpleMessageRetryStrategy extends AbstractMessageRetryStrategy {

    private final int times;

    private final int period;

    @Override
    public int getPeriod(int current) {
        return period;
    }
}
