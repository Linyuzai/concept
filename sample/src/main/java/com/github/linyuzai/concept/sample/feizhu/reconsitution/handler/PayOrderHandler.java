package com.github.linyuzai.concept.sample.feizhu.reconsitution.handler;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.payment.OrderPayment;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.payment.OrderPaymentAdapter;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizType;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.retry.PayRetryAdapter;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.retry.Retry;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatusIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@OrderStatusIn(OrderStatus.CREATED)
public class PayOrderHandler extends AbstractOrderStatusHandler {

    @Autowired
    private OrderPaymentAdapter orderPaymentAdapter;

    @Autowired
    private PayRetryAdapter payRetryAdapter;

    @Override
    public void onHandle(OrderContext context) {
        OrderBizType bizType = context.get(OrderBizType.class);
        OrderPayment payment = orderPaymentAdapter.getOrderPayment(bizType);
        if (payment == null) {
            throw new UnsupportedOperationException(bizType.toString() + " not support");
        } else {
            Retry<Long> retry = payRetryAdapter.getRetry(bizType);
            payment.setRetry(retry);
            long payId = payment.pay(context);
            context.put(OrderContext.Key.PAY_ID, payId);
            if (payId < 0) {
                payment.close(context);
            }
        }
    }
}
