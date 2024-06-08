package com.github.linyuzai.plugin.core.read;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class DependencyReader implements PluginReader {

    //TODO 正则匹配
    private final Set<String> dependencies = new LinkedHashSet<>();

    public DependencyReader(Plugin plugin) throws IOException {
        dependencies.addAll(getDependencies(plugin));
    }

    @Override
    public Object read(Object key, PluginContext context) {
        Object doRead = doRead(key);
        if (doRead != null) {
            return doRead;
        }
        for (Plugin plugin : context.getConcept().getPlugins().values()) {
            Plugin.Metadata metadata = plugin.getMetadata();
            String name = metadata.get("concept.plugin.name");
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

    public List<String> getDependencies(Plugin plugin) throws IOException {
        Plugin.Metadata metadata = plugin.getMetadata();
        String classes = metadata.get("concept.plugin.dependency.class");
        if (classes == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(classes.split(","))
                .collect(Collectors.toList());
    }

    @Override
    public boolean support(Class<?> readable) {
        return getReadableType() == readable;
    }
}
