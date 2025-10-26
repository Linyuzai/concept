package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.jar.handle.resolve.ClassSupplier;

import java.util.function.Supplier;

/**
 * Bean 延迟加载对象
 */
public interface BeanSupplier extends Supplier<Object> {

    ClassSupplier getClassSupplier();
}
