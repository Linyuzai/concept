package com.github.linyuzai.concept.sample.feizhu.reconsitution;

import com.github.linyuzai.concept.sample.feizhu.OrderInfo;
import com.github.linyuzai.concept.sample.feizhu.OrderService;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizType;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContextFactory;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.handler.OrderStatusHandler;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.handler.OrderStatusHandlerAdapter;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor.OrderStatusHandleInterceptorChainFactory;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatusHandledEvent;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatusUnhandledEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusMQListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderContextFactory contextFactory;

    @Autowired
    private OrderStatusHandlerAdapter handlerAdapter;

    @Autowired
    private OrderStatusHandleInterceptorChainFactory chainFactory;

    public void consume(String orderNo) {
        final OrderInfo orderInfo = orderService.getOrderInfo(orderNo);
        // 订单业务类型
        final OrderBizType bizType = OrderBizType.from(orderInfo.getOrderBizType());
        // 订单状态
        final OrderStatus status = OrderStatus.from(orderInfo.getStatus());
        OrderContext context = contextFactory.create();
        context.put(OrderInfo.class, orderInfo);
        context.put(OrderBizType.class, bizType);
        context.put(OrderStatus.class, status);
        context.put(OrderStatusHandleInterceptorChainFactory.class, chainFactory);
        OrderStatusHandler handler = handlerAdapter.getOrderStatusHandler(status);
        if (handler == null) {
            context.publishEvent(new OrderStatusUnhandledEvent(orderInfo));
        } else {
            handler.handle(context);
            context.publishEvent(new OrderStatusHandledEvent(orderInfo));
        }
    }
}
