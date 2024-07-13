package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 插件仓储抽象类
 * <p>
 * 使用 {@link Map} 存储
 */
public abstract class AbstractPluginRepository implements PluginRepository {

    private final Map<Object, Plugin> plugins = createMap();

    /**
     * 通过插件的 id，source 获得插件
     */
    @Override
    public Plugin get(Object o) {
        Plugin plugin = plugins.get(o);
        if (plugin != null) {
            return plugin;
        }
        for (Plugin value : plugins.values()) {
            if (Objects.equals(o, value.getId())) {
                return value;
            }
        }
        return null;
    }

    /**
     * 以 source 为 key 存储插件
     */
    @Override
    public void add(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugins.put(plugin.getSource(), plugin);
    }

    /**
     * 根据 id，source 或插件本身移除插件
     */
    @Override
    public Plugin remove(Object o) {
        Plugin plugin = plugins.remove(o);
        if (plugin != null) {
            return plugin;
        }
        Iterator<Plugin> iterator = plugins.values().iterator();
        while (iterator.hasNext()) {
            Plugin next = iterator.next();
            if (Objects.equals(next.getId(), o) || Objects.equals(next, o)) {
                iterator.remove();
                return next;
            }
        }
        return null;
    }

    /**
     * 根据 id，source 或插件本身判断是否存在
     */
    @Override
    public boolean contains(Object o) {
        return plugins.containsKey(o) || plugins.values()
                .stream()
                .map(Plugin::getId)
                .collect(Collectors.toSet()).contains(o) ||
                (o instanceof Plugin && plugins.containsValue(o));
    }

    @Override
    public Stream<Plugin> stream() {
        return plugins.values().stream();
    }

    /**
     * 创建存储插件的 Map
     */
    protected abstract Map<Object, Plugin> createMap();
}
