package com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor;

import com.github.linyuzai.concept.sample.feizhu.OrderInfo;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizType;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class PayAfterOfVisaBusinessHandleInterceptor implements OrderStatusHandleInterceptor {

    @Override
    public void intercept(OrderContext context, Consumer<OrderContext> consumer, OrderStatusHandleInterceptorChain chain) {
        chain.next(context, consumer);
        OrderBizType bizType = context.get(OrderBizType.class);
        if (bizType == OrderBizType.VISA) {
            Long payId = context.get(OrderContext.Key.PAY_ID);
            if (payId > 0) {
                OrderInfo orderInfo = context.get(OrderInfo.class);
                payAfterOfVisaBusiness(payId, orderInfo);
            }
        }
    }

    private void payAfterOfVisaBusiness(long payId, OrderInfo orderInfo) {
        // 各种签证订单支付后的逻辑 ...... 候选人不用关系内部逻辑
    }
}
