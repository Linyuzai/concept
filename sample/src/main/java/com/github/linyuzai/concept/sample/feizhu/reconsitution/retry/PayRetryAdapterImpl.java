package com.github.linyuzai.concept.sample.feizhu.reconsitution.retry;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PayRetryAdapterImpl implements PayRetryAdapter {

    private final Map<OrderBizType, Retry<?>> retryMap = new ConcurrentHashMap<>();

    public PayRetryAdapterImpl() {
        retryMap.put(OrderBizType.TICKETS, new TimesRetry<>(3));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Retry<T> getRetry(OrderBizType bizType) {
        return (Retry<T>) retryMap.get(bizType);
    }
}
