package com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
public class OrderStatusHandleInterceptorChainImpl implements OrderStatusHandleInterceptorChain {

    private int index;

    private final List<OrderStatusHandleInterceptor> interceptors;

    @Override
    public void next(OrderContext context, Consumer<OrderContext> consumer) {
        if (index < interceptors.size()) {
            OrderStatusHandleInterceptor interceptor = interceptors.get(index++);
            interceptor.intercept(context, consumer, this);
        } else {
            consumer.accept(context);
        }
    }
}
