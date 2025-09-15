package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;

import java.util.Map;
import java.util.stream.Stream;

/**
 * 插件仓储抽象类
 * <p>
 * 使用 {@link Map} 存储
 */
public abstract class AbstractPluginRepository implements PluginRepository {

    private final Map<String, Plugin> plugins = createMap();

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

    @Override
    public Stream<Plugin> stream() {
        return plugins.values().stream();
    }

    /**
     * 创建存储插件的 Map
     */
    protected abstract Map<String, Plugin> createMap();
}
