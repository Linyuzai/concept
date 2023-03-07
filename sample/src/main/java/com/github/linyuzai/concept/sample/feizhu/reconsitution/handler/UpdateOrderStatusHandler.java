package com.github.linyuzai.concept.sample.feizhu.reconsitution.handler;

import com.github.linyuzai.concept.sample.feizhu.OrderInfo;
import com.github.linyuzai.concept.sample.feizhu.OrderService;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatusIn;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@OrderStatusIn(OrderStatus.COMPLETED)
public class UpdateOrderStatusHandler extends AbstractOrderStatusHandler {

    @Autowired
    private OrderService orderService;

    @Override
    public void onHandle(OrderContext context) {
        OrderInfo orderInfo = context.get(OrderInfo.class);
        orderService.updateOrderStatus(orderInfo.getOrderNo(), OrderStatus.COMPLETED.getValue());
    }
}
