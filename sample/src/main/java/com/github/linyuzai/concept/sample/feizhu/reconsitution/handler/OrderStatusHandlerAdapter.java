package com.github.linyuzai.concept.sample.feizhu.reconsitution.handler;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;

public interface OrderStatusHandlerAdapter {

    OrderStatusHandler getOrderStatusHandler(OrderStatus status);
}
