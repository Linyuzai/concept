package com.github.linyuzai.plugin.core.read;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class DependencyReader implements PluginReader {

    private final Plugin plugin;

    public DependencyReader(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Object read(Object key, PluginContext context) {
        if (context.contains(getPlugin())) {
            return null;
        }
        context.set(getPlugin(), Boolean.TRUE);
        Object doRead = doRead(key);
        if (doRead != null) {
            return doRead;
        }
        return readDependency(key, context);
    }

    protected Object readDependency(Object key, PluginContext context) {
        List<Plugin> plugins = context.getRoot()
                .getConcept()
                .getRepository()
                .stream()
                .collect(Collectors.toList());
        Set<String> dependencies = getDependencies();
        for (Plugin plugin : plugins) {
            Plugin.StandardMetadata metadata = plugin.getMetadata().standard();
            String name = metadata.getName();
            if (name == null || name.isEmpty()) {
                continue;
            }
            if (dependencies.contains(name)) {
                Object read = plugin.read(getReadableType(), key, context);
                if (read != null) {
                    return read;
                }
            }
        }
        return null;
    }

    public abstract Object doRead(Object key);

    public abstract Class<?> getReadableType();

    public Set<String> getDependencies() {
        Plugin.StandardMetadata metadata = plugin.getMetadata().standard();
        Set<String> names = metadata.getDependency().getNames();
        return names == null ? Collections.emptySet() : names;
    }

    @Override
    public boolean support(Class<?> readable) {
        return getReadableType() == readable;
    }
}
