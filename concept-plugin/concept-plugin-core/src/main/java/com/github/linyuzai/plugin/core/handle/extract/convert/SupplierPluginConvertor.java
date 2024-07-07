package com.github.linyuzai.plugin.core.handle.extract.convert;

import java.util.function.Supplier;

public class SupplierPluginConvertor<T> extends AbstractPluginConvertor<Supplier<T>, T> {

    @Override
    public T doConvert(Supplier<T> source) {
        return source.get();
    }
}
