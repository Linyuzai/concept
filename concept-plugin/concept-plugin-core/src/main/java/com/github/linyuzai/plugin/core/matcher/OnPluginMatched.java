package com.github.linyuzai.plugin.core.matcher;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnPluginMatched {
}
