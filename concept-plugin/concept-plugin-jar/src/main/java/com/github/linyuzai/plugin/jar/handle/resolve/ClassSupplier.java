package com.github.linyuzai.plugin.jar.handle.resolve;

import java.util.function.Supplier;

/**
 * 延迟加载的类
 */
public interface ClassSupplier extends Supplier<Class<?>> {

    /**
     * 获得类名
     */
    String getName();
}
