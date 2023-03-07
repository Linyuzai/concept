package com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatusSupporter;

import java.util.function.Consumer;

public interface OrderStatusHandleInterceptor extends OrderStatusSupporter {

    void intercept(OrderContext context, Consumer<OrderContext> consumer, OrderStatusHandleInterceptorChain chain);
}
