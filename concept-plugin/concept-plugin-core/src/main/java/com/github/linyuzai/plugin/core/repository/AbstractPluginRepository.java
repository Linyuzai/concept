package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

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
     * 通过插件的 id，source 获得插件
     */
    @Override
    public Plugin get(String key) {
        return plugins.get(key);
    }

    /**
     * 以 source 为 key 存储插件
     */
    @Override
    public void add(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugins.put(plugin.getDefinition().getPath(), plugin);
    }

    /**
     * 根据 id，source 或插件本身移除插件
     */
    @Override
    public Plugin remove(String path) {
        return plugins.remove(path);
    }

    /**
     * 根据 id，source 或插件本身判断是否存在
     */
    @Override
    public boolean contains(String path) {
        return plugins.containsKey(path);
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
