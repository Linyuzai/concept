package com.github.linyuzai.concept.sample.feizhu.reconsitution.handler;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.context.OrderContext;
import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatusSupporter;

public interface OrderStatusHandler extends OrderStatusSupporter {

    void handle(OrderContext context);
}
