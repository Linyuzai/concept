package com.github.linyuzai.concept.sample.feizhu.reconsitution.handler;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor.OrderStatusHandleInterceptorChain;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor.OrderStatusHandleInterceptorChainFactory;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;

public abstract class AbstractOrderStatusHandler implements OrderStatusHandler {

    @Override
    public void handle(OrderContext context) {
        OrderStatusHandleInterceptorChainFactory chainFactory =
                context.get(OrderStatusHandleInterceptorChainFactory.class);
        OrderStatusHandleInterceptorChain chain =
                chainFactory.create(context.get(OrderStatus.class));
        chain.next(context, this::onHandle);
    }

    public abstract void onHandle(OrderContext context);
}
