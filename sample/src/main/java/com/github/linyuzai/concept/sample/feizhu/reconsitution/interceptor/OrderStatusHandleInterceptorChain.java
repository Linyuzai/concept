package com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;

import java.util.function.Consumer;

public interface OrderStatusHandleInterceptorChain {

    void next(OrderContext context, Consumer<OrderContext> consumer);
}
