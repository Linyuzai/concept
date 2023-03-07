package com.github.linyuzai.concept.sample.feizhu.reconsitution.retry;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype.OrderBizType;

public interface PayRetryAdapter {

   <T> Retry<T> getRetry(OrderBizType bizType);
}
