package com.github.linyuzai.plugin.core.storage;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.util.ReadUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

public class MemoryPluginStorage extends AbstractPluginStorage {

    private final Map<String, String> types = new HashMap<>();

    private final Map<String, Map<String, PluginDefinition>> plugins = new LinkedHashMap<>();

    @Override
    public String getLocation() {
        return "";
    }

    @Override
    public synchronized List<String> getGroups() {
        return new ArrayList<>(plugins.keySet());
    }

    @Override
    public synchronized void addGroup(String group) {
        plugins.computeIfAbsent(group, f -> new LinkedHashMap<>());
    }

    @Override
    public synchronized PluginDefinition getPluginDefinition(String type, String group, String name) {
        PluginDefinition definition = plugins.getOrDefault(group, Collections.emptyMap()).get(name);
        if (definition == null) {
            return new PluginDefinitionImpl(getPluginPath(group, name), name, null);
        } else {
            return definition;
        }
    }

    @Override
    public synchronized Stream<PluginDefinition> getPluginDefinitions(String type, String group) {
        return plugins.getOrDefault(group, Collections.emptyMap())
                .values()
                .stream()
                .filter(it -> type.equals(types.get(it.getPath())));
    }

    @SneakyThrows
    @Override
    public synchronized String uploadPlugin(String group, String name, InputStream is, long length) {
        String generated = generateName(group, name);
        String path = getPluginPath(group, generated);
        PluginDefinition definition = new PluginDefinitionImpl(path, name, ReadUtils.read(is));
        types.put(path, UNLOADED);
        plugins.get(group).put(generated, definition);
        return generated;
    }

    @Override
    public synchronized void loadPlugin(String group, String name) {
        updatePluginInfo(group, name, LOADED);
    }

    @Override
    public synchronized void unloadPlugin(String group, String name) {
        updatePluginInfo(group, name, UNLOADED);
    }

    @Override
    public synchronized void deletePlugin(String group, String name) {
        updatePluginInfo(group, name, DELETED);
        renamePlugin(group, name, generateDeletedName(group, name));
    }

    @Override
    public synchronized boolean existPlugin(String group, String name) {
        return plugins.getOrDefault(group, Collections.emptyMap()).containsKey(name);
    }

    @SneakyThrows
    @Override
    public synchronized void renamePlugin(String group, String name, String rename) {
        if (existPlugin(group, rename)) {
            throw new IllegalArgumentException("Name existed");
        }
        PluginDefinition definition = plugins.getOrDefault(group, Collections.emptyMap()).remove(rename);
        if (definition == null) {
            return;
        }
        String path = getPluginPath(group, name);
        String newPath = getPluginPath(group, rename);
        types.put(newPath, types.remove(path));
        plugins.get(group).put(rename, new PluginDefinitionImpl(newPath, rename,
                ReadUtils.read(definition.getInputStream())));
    }

    protected String getPluginPath(String group, String name) {
        return group + "/" + name;
    }

    protected String generateName(String group, String name) {
        return generateName(name, n -> plugins.getOrDefault(group, Collections.emptyMap()).containsKey(n));
    }

    protected String generateDeletedName(String group, String name) {
        return generateDeletedName(name, n -> plugins.getOrDefault(group, Collections.emptyMap()).containsKey(n));
    }

    protected void updatePluginInfo(String group, String name, String type) {
        types.put(getPluginPath(group, name), type);
    }

    @Getter
    @RequiredArgsConstructor
    public static class PluginDefinitionImpl implements PluginDefinition {

        private final String path;

        private final String name;

        private final byte[] bytes;

        private final long createTime = System.currentTimeMillis();

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public Object getVersion() {
            return createTime;
        }

        @Override
        public InputStream getInputStream() {
            if (bytes == null) {
                throw new PluginException("Cannot read plugin bytes: " + path);
            }
            return new ByteArrayInputStream(bytes);
        }
    }
}
