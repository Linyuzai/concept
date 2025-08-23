package com.github.linyuzai.tx.core.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoTransactional {

    String concept() default "";
}
