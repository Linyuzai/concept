package com.github.linyuzai.concept.sample.feizhu.reconsitution.status;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderStatusIn {

    OrderStatus[] value();
}
