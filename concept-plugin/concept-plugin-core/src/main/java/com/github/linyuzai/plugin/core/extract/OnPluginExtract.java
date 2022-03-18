package com.github.linyuzai.plugin.core.extract;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnPluginExtract {
}
