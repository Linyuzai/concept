package com.github.linyuzai.plugin.jar.handle.extract.match;

import java.lang.annotation.*;

/**
 * 用于匹配类上的注解
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginAnnotation {

    Class<? extends Annotation>[] value();
}
