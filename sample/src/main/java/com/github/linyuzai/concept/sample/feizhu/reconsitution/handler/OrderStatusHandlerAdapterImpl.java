package com.github.linyuzai.concept.sample.feizhu.reconsitution.handler;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderStatusHandlerAdapterImpl implements OrderStatusHandlerAdapter {

    private final List<OrderStatusHandler> handlers;

    @Override
    public OrderStatusHandler getOrderStatusHandler(OrderStatus status) {
        for (OrderStatusHandler handler : handlers) {
            if (handler.support(status)) {
                return handler;
            }
        }
        return null;
    }
}
