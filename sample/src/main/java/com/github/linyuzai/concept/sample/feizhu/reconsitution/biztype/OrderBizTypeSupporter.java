package com.github.linyuzai.concept.sample.feizhu.reconsitution.biztype;

public interface OrderBizTypeSupporter {

    default boolean support(OrderBizType bizType) {
        OrderBizTypeIn in = getClass().getAnnotation(OrderBizTypeIn.class);
        if (in == null) {
            return false;
        }
        OrderBizType[] bizTypes = in.value();
        for (OrderBizType t : bizTypes) {
            if (t == bizType) {
                return true;
            }
        }
        return false;
    }
}
