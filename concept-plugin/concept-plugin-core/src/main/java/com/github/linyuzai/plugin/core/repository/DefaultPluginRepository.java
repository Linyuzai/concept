package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultPluginRepository implements PluginRepository {

    private final Map<Object, Plugin> plugins = new ConcurrentHashMap<>();

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

    @Override
    public void add(Object o, Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugins.put(o, plugin);
    }

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
}
