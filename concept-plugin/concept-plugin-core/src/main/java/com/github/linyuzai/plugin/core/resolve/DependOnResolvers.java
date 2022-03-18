package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.resolve.PluginResolver;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DependOnResolvers {

    Class<? extends PluginResolver>[] value() default {};
}
