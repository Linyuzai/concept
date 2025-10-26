package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 默认插件仓储
 */
public class DefaultPluginRepository implements PluginRepository {

    private final Map<String, Plugin> plugins = new ConcurrentHashMap<>();

    /**
     * 获得插件
     */
    @Override
    public Plugin get(PluginDefinition definition) {
        return plugins.get(definition.getPath());
    }

    /**
     * 添加插件
     */
    @Override
    public void add(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugins.put(plugin.getDefinition().getPath(), plugin);
    }

    /**
     * 移除插件
     */
    @Override
    public Plugin remove(PluginDefinition definition) {
        return plugins.remove(definition.getPath());
    }

    /**
     * 插件是否存在
     */
    @Override
    public boolean contains(PluginDefinition definition) {
        return plugins.containsKey(definition.getPath());
    }

    /**
     * 获得插件流
     */
    @Override
    public Stream<Plugin> stream() {
        return plugins.values().stream();
    }
}
