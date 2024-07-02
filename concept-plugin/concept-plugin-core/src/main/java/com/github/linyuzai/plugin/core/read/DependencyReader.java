package com.github.linyuzai.plugin.core.read;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class DependencyReader implements PluginReader {

    private final Plugin plugin;

    //TODO 正则匹配
    private final Set<String> dependencies = new LinkedHashSet<>();

    public DependencyReader(Plugin plugin) {
        this.plugin = plugin;
        dependencies.addAll(Arrays.asList(getDependencies(plugin)));
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
        List<Plugin> plugins = context.getConcept()
                .getRepository()
                .stream()
                .collect(Collectors.toList());
        for (Plugin plugin : plugins) {
            PluginMetadata metadata = plugin.getMetadata();
            String name = metadata.property(Plugin.MetadataProperties.NAME);
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

    public String[] getDependencies(Plugin plugin) {
        PluginMetadata metadata = plugin.getMetadata();
        return metadata.property(Plugin.MetadataProperties.DEPENDENCY_NAMES);
    }

    @Override
    public boolean support(Class<?> readable) {
        return getReadableType() == readable;
    }
}
