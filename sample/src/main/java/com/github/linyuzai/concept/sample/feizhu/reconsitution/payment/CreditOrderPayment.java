package com.github.linyuzai.concept.sample.feizhu.reconsitution.payment;

import com.github.linyuzai.concept.sample.feizhu.OrderInfo;
import com.github.linyuzai.concept.sample.feizhu.PayService;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditOrderPayment extends AbstractOrderPayment {

    @Autowired
    private PayService payService;

    @Override
    protected long doPay(OrderContext context) {
        OrderInfo orderInfo = context.get(OrderInfo.class);
        return payService.payOfCredit(orderInfo);
    }

    @Override
    public void close(OrderContext context) {
        OrderInfo orderInfo = context.get(OrderInfo.class);
        payService.notPayedToClose(orderInfo);
    }
}
