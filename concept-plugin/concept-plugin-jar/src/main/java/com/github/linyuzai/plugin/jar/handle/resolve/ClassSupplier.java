package com.github.linyuzai.plugin.jar.handle.resolve;

import java.util.function.Supplier;

public interface ClassSupplier extends Supplier<Class<?>> {

    String getName();
}
