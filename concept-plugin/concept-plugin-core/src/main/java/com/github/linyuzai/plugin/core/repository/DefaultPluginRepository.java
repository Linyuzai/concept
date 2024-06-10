package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class DefaultPluginRepository implements PluginRepository {

    private final Map<Object, Plugin> plugins = new ConcurrentHashMap<>();

    @Override
    public Plugin get(Object id) {
        return plugins.get(id);
    }

    @Override
    public void add(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugins.put(plugin.getId(), plugin);
    }

    @Override
    public Plugin remove(Object plugin) {
        if (plugin instanceof Plugin) {
            return plugins.remove(((Plugin) plugin).getId());
        } else {
            return plugins.remove(plugin);
        }
    }

    @Override
    public boolean contains(Object plugin) {
        if (plugin instanceof Plugin) {
            return plugins.containsValue(plugin);
        } else {
            return plugins.containsKey(plugin);
        }
    }

    @Override
    public Stream<Plugin> stream() {
        return plugins.values().stream();
    }
}
