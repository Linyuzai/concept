package com.github.linyuzai.plugin.core.storage;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MemoryPluginStorage extends AbstractPluginStorage {

    private final Set<String> groups = new LinkedHashSet<>();

    private final Map<String, String> types = new HashMap<>();

    private final Map<String, PluginDefinition> plugins = new HashMap<>();

    @Override
    public void initialize() {

    }

    @Override
    public String getLocation() {
        return "";
    }

    @Override
    public synchronized List<String> getGroups() {
        return new ArrayList<>(groups);
    }

    @Override
    public synchronized void addGroup(String group) {
        groups.add(group);
    }

    @Override
    public synchronized PluginDefinition getPluginDefinition(String type, String group, String name) {
        String path = getPluginPath(group, name);
        PluginDefinition definition = plugins.get(path);
        if (definition == null) {
            return new PluginDefinitionImpl(path, null);
        } else {
            return definition;
        }
    }

    @Override
    public synchronized Map<String, PluginDefinition> getPluginDefinitions(String type, String group) {
        return plugins.keySet().stream()
                .filter(it -> type.equals(types.get(it)))
                .filter(it -> it.startsWith(group + "/"))
                .map(it -> it.substring(group.length() + 1))
                .collect(Collectors.toMap(Function.identity(), it -> getPluginDefinition(type, group, it)));
    }

    @SneakyThrows
    @Override
    public synchronized String uploadPlugin(String group, String name, InputStream is, long length) {
        String path = generateName(getPluginPath(group, name));
        PluginDefinition definition = new PluginDefinitionImpl(path, PluginStorage.read(is));
        types.put(path, UNLOADED);
        plugins.put(path, definition);
        return name;
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
        renamePlugin(group, name, generateDeletedName(getPluginPath(group, name)));
    }

    @Override
    public synchronized boolean existPlugin(String group, String name) {
        return plugins.containsKey(getPluginPath(group, name));
    }

    @SneakyThrows
    @Override
    public synchronized void renamePlugin(String group, String name, String rename) {
        if (existPlugin(group, rename)) {
            throw new IllegalArgumentException("Name existed");
        }
        String path = getPluginPath(group, name);
        PluginDefinition definition = plugins.remove(path);
        if (definition == null) {
            return;
        }
        String newPath = getPluginPath(group, rename);
        types.put(newPath, types.remove(path));
        plugins.put(newPath, new PluginDefinitionImpl(newPath,
                PluginStorage.read(Objects.requireNonNull(definition.getInputStream()))));
    }

    protected String getPluginPath(String group, String name) {
        return group + "/" + name;
    }

    protected String generateName(String path) {
        return generateName(path, plugins::containsKey);
    }

    protected String generateDeletedName(String path) {
        return generateDeletedName(path, plugins::containsKey);
    }

    protected void updatePluginInfo(String group, String name, String type) {
        types.put(getPluginPath(group, name), type);
    }

    @Getter
    @RequiredArgsConstructor
    public static class PluginDefinitionImpl implements PluginDefinition {

        private final String path;

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

        @Nullable
        @Override
        public InputStream getInputStream() {
            if (bytes == null) {
                return null;
            }
            return new ByteArrayInputStream(bytes);
        }
    }
}
