package com.github.linyuzai.plugin.autoconfigure.observable;

import java.util.function.BiConsumer;

public interface PluginObservable<K, V> {

    V get(K key);

    void forEach(BiConsumer<? super K, ? super V> action);
}
