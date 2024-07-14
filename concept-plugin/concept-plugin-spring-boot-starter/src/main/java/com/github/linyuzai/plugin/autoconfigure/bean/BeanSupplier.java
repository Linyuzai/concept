package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.jar.handle.resolve.ClassSupplier;

import java.util.function.Supplier;

/**
 * Bean
 */
public interface BeanSupplier extends Supplier<Object> {

    ClassSupplier getClassSupplier();
}
