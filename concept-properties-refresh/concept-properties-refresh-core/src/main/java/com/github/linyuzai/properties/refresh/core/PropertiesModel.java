package com.github.linyuzai.properties.refresh.core;

import java.lang.annotation.*;

/**
 * 属性模型注解，可以添加在包含属性的类或接口上
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertiesModel {

}
