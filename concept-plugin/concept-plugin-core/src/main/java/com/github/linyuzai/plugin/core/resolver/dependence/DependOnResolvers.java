package com.github.linyuzai.plugin.core.resolver.dependence;

import com.github.linyuzai.plugin.core.resolver.PluginResolver;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DependOnResolvers {

    Class<? extends PluginResolver>[] value() default {};
}
