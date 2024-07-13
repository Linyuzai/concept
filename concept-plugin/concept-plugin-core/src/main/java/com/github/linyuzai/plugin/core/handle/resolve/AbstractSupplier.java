package com.github.linyuzai.plugin.core.handle.resolve;

import java.util.function.Supplier;

/**
 * 延迟加载的内容
 */
public abstract class AbstractSupplier<T> implements Supplier<T> {

    private volatile T supplied;

    @Override
    public T get() {
        if (supplied == null) {
            synchronized (this) {
                if (supplied == null) {
                    supplied = create();
                }
            }
        }
        return supplied;
    }

    public abstract T create();
}
