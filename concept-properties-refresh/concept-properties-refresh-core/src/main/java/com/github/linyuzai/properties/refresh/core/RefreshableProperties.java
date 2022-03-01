package com.github.linyuzai.properties.refresh.core;

import java.lang.annotation.*;

/**
 * 标注在字段上可以实现属性自动刷新 {@link FieldPropertiesRefresher}
 * 和 {@link OnPropertiesRefresh} 一起使用，标注在方法参数上可以实现刷新回调 {@link MethodPropertiesRefresher}
 * 标注在接口方法上可以实现动态读取或自动刷新
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefreshableProperties {

    /**
     * @return 匹配的key
     */
    String value() default "";
}
