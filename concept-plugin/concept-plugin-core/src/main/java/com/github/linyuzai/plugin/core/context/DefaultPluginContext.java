package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link PluginContext} 默认实现。
 * 通过 {@link LinkedHashMap} 缓存数据。
 */
@SuppressWarnings("unchecked")
public class DefaultPluginContext implements PluginContext {

    private final PluginConcept concept;

    private final Plugin plugin;

    private final Map<Object, Object> map = new LinkedHashMap<>();

    public DefaultPluginContext(@NonNull PluginConcept concept, @NonNull Plugin plugin) {
        this.concept = concept;
        this.plugin = plugin;
    }

    @Override
    public <C extends PluginConcept> C getPluginConcept() {
        return (C) concept;
    }

    @Override
    public <P extends Plugin> P getPlugin() {
        return (P) plugin;
    }

    @Override
    public <T> T get(Object key) {
        return (T) map.get(key);
    }

    @Override
    public void set(Object key, Object value) {
        map.put(key, value);
    }

    @Override
    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {
        map.clear();
    }
}
