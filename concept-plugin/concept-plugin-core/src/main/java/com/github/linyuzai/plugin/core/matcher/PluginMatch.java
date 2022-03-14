package com.github.linyuzai.plugin.core.matcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginMatch {

    String[] path() default {};

    String[] name() default {};

    String[] prefix() default {};

    String[] suffix() default {};

    String[] regex() default {};
}
