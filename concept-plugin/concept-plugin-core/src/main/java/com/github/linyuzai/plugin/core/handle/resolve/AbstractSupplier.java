package com.github.linyuzai.plugin.core.handle.resolve;

import com.github.linyuzai.plugin.core.sync.SyncSupport;

import java.util.function.Supplier;

/**
 * 延迟加载的内容
 */
public abstract class AbstractSupplier<T> extends SyncSupport implements Supplier<T> {

    private volatile T supplied;

    @Override
    public T get() {
        if (supplied == null) {
            syncWrite(() -> {
                if (supplied == null) {
                    supplied = create();
                }
            });
        }
        return supplied;
    }

    /**
     * 创建对象实例
     */
    public abstract T create();
}
