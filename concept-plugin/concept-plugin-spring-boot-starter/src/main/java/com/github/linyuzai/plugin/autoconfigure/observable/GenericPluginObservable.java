package com.github.linyuzai.plugin.autoconfigure.observable;

import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.context.PluginContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class GenericPluginObservable<K, V> extends BeanExtractor<V> implements PluginObservable<K, V> {

    private final Map<K, V> plugins = createMap();

    @Override
    public V get(K key) {
        return plugins.get(key);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        plugins.forEach(action);
    }

    @Override
    public void onExtract(V plugin, PluginContext context) {
        K key = mappingKey(plugin, context);
        context.getPlugin().addDestroyListener(p -> plugins.remove(key));
        plugins.compute(key, (k, v) -> {
            if (v == null) {
                return plugin;
            } else {
                return mergingValue(k, v, plugin, context);
            }
        });
    }

    protected Map<K, V> createMap() {
        return new ConcurrentHashMap<>();
    }

    public abstract K mappingKey(V plugin, PluginContext context);

    public abstract V mergingValue(K key, V exist, V loaded, PluginContext context);
}
