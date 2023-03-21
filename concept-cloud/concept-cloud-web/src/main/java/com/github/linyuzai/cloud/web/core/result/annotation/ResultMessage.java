package com.github.linyuzai.cloud.web.core.result.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在方法上
 * <p>
 * 指定成功和失败的信息
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultMessage {

    /**
     * 成功信息
     *
     * @return 成功信息
     */
    String success() default "";

    /**
     * 失败信息
     *
     * @return 失败信息
     */
    String failure() default "";
}
