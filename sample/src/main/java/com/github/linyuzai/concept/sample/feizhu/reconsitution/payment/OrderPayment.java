package com.github.linyuzai.concept.sample.feizhu.reconsitution.payment;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizTypeSupporter;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.retry.Retry;

public interface OrderPayment extends OrderBizTypeSupporter {

    long pay(OrderContext context);

    void close(OrderContext context);

    void setRetry(Retry<Long> retry);
}
