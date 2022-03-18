package com.github.linyuzai.plugin.jar.match;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginAnnotation {

    Class<? extends Annotation>[] value();
}
