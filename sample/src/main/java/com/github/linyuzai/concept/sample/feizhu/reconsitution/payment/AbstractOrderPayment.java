package com.github.linyuzai.concept.sample.feizhu.reconsitution.payment;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.retry.Retry;
import lombok.Getter;

@Getter
public abstract class AbstractOrderPayment implements OrderPayment {

    private Retry<Long> retry;

    @Override
    public long pay(OrderContext context) {
        long payId = doPay(context);
        if (payId > 0 || retry == null) {
            return payId;
        }
        return retry.retry(() -> doPay(context), id -> id > 0, -1L);
    }

    protected abstract long doPay(OrderContext context);

    @Override
    public void setRetry(Retry<Long> retry) {
        this.retry = retry;
    }
}
