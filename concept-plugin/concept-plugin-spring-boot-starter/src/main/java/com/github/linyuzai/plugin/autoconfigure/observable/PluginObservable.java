package com.github.linyuzai.plugin.autoconfigure.observable;

public interface PluginObservable<K, P> {

    P get(K key);
}
