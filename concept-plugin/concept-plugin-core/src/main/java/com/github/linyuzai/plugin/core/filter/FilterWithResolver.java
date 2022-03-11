package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.resolver.PluginResolver;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilterWithResolver {

    Class<? extends PluginResolver> value();
}
