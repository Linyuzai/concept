package com.github.linyuzai.concept.sample.feizhu.reconsitution.notify;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizTypeSupporter;

public interface OrderNotifier extends OrderBizTypeSupporter {

    void notify(String msg);
}
