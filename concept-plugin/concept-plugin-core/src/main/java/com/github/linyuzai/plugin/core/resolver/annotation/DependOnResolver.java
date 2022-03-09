package com.github.linyuzai.plugin.core.resolver.annotation;

import com.github.linyuzai.plugin.core.resolver.PluginResolver;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DependOnResolver {

    Class<? extends PluginResolver>[] value() default {};
}
