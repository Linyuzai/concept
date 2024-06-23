package com.github.linyuzai.plugin.core.read;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.util.*;
import java.util.stream.Collectors;

public abstract class DependencyReader implements PluginReader {

    //TODO 正则匹配
    private final Set<String> dependencies = new LinkedHashSet<>();

    public DependencyReader(Plugin plugin) {
        dependencies.addAll(getDependencies(plugin));
    }

    @Override
    public Object read(Object key, PluginContext context) {
        Object doRead = doRead(key);
        if (doRead != null) {
            return doRead;
        }
        List<Plugin> plugins = context.getConcept()
                .getRepository()
                .stream()
                .collect(Collectors.toList());
        for (Plugin plugin : plugins) {
            Plugin.Metadata metadata = plugin.getMetadata();
            String name = metadata.get(Plugin.Metadata.PropertyKey.NAME);
            if (name == null || name.isEmpty()) {
                continue;
            }
            if (dependencies.contains(name)) {
                Object read = plugin.read(getReadableType(), key);
                if (read != null) {
                    return read;
                }
            }
        }
        return null;
    }

    public abstract Object doRead(Object key);

    public abstract Class<?> getReadableType();

    public List<String> getDependencies(Plugin plugin) {
        Plugin.Metadata metadata = plugin.getMetadata();
        String dependencies = metadata.get(Plugin.Metadata.PropertyKey.DEPENDENCY_NAMES);
        if (dependencies == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(dependencies.split(","))
                .collect(Collectors.toList());
    }

    @Override
    public boolean support(Class<?> readable) {
        return getReadableType() == readable;
    }
}
