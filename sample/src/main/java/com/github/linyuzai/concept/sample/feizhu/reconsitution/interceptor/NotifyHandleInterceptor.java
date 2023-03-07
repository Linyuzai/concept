package com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.notify.OrderNotifierAdapter;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatusIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@OrderStatusIn(OrderStatus.COMPLETED)
public class NotifyHandleInterceptor implements OrderStatusHandleInterceptor {

    @Autowired
    private OrderNotifierAdapter notifierAdapter;

    @Override
    public void intercept(OrderContext context, Consumer<OrderContext> consumer, OrderStatusHandleInterceptorChain chain) {
        chain.next(context, consumer);
        notifierAdapter.getNotifier(null);
    }
}
