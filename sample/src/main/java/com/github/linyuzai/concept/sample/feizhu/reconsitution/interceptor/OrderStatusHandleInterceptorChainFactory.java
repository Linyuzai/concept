package com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;

public interface OrderStatusHandleInterceptorChainFactory {

    OrderStatusHandleInterceptorChain create(OrderStatus status);
}
