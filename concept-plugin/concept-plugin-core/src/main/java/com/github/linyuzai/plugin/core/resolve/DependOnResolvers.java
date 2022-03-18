package com.github.linyuzai.plugin.core.resolve;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DependOnResolvers {

    Class<? extends PluginResolver>[] value() default {};
}
