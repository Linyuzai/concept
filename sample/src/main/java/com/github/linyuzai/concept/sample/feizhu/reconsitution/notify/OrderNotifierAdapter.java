package com.github.linyuzai.concept.sample.feizhu.reconsitution.notify;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizType;

import java.util.List;

public interface OrderNotifierAdapter {

    List<OrderNotifier> getNotifier(OrderBizType bizType);
}
