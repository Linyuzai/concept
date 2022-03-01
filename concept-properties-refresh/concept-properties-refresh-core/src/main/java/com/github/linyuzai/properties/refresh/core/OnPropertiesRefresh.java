package com.github.linyuzai.properties.refresh.core;

import java.lang.annotation.*;

/**
 * 标注在方法上则表明属性刷新时自动回调该方法
 * 和 {@link RefreshableProperties} 搭配使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnPropertiesRefresh {

}
