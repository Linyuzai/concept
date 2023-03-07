package com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor;

import com.github.linyuzai.concept.sample.feizhu.OrderInfo;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizType;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatusIn;

import java.util.function.Consumer;

@OrderStatusIn(OrderStatus.CREATED)
public class PayPrdOfHotelVerifyHandleInterceptor implements OrderStatusHandleInterceptor {

    @Override
    public void intercept(OrderContext context, Consumer<OrderContext> consumer, OrderStatusHandleInterceptorChain chain) {
        OrderBizType bizType = context.get(OrderBizType.class);
        if (bizType == OrderBizType.HOTEL) {
            OrderInfo orderInfo = context.get(OrderInfo.class);
            payPrdOfHotelVerify(orderInfo);
        }
        chain.next(context, consumer);
    }

    private void payPrdOfHotelVerify(OrderInfo orderInfo) {
        // 各种酒店订单支付前的检查 ...... 候选人不用关系内部逻辑
    }
}
