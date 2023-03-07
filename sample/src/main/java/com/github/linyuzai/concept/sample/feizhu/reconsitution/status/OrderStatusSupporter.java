package com.github.linyuzai.concept.sample.feizhu.reconsitution.status;

public interface OrderStatusSupporter {

    default boolean support(OrderStatus status) {
        OrderStatusIn in = getClass().getAnnotation(OrderStatusIn.class);
        if (in == null) {
            return false;
        }
        OrderStatus[] statuses = in.value();
        for (OrderStatus s : statuses) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}
