package com.github.linyuzai.concept.sample.feizhu.reconsitution.payment;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizType;

public interface OrderPaymentAdapter {

    OrderPayment getOrderPayment(OrderBizType bizType);
}
