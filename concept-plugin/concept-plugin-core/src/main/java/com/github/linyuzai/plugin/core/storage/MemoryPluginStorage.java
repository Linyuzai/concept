package com.github.linyuzai.plugin.core.storage;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MemoryPluginStorage implements PluginStorage {

    private final Map<String, Map<String, PluginInfo>> plugins = new LinkedHashMap<>();

    @Override
    public void initialize() {

    }

    @Override
    public String getLocation() {
        return "";
    }

    @Override
    public List<String> getGroups() {
        return new ArrayList<>(plugins.keySet());
    }

    @Override
    public void addGroup(String group) {
        plugins.computeIfAbsent(group, k -> new HashMap<>());
    }


    @Override
    public PluginDefinition getPluginDefinition(String type, String group, String name) {
        PluginInfo info = plugins.getOrDefault(group, Collections.emptyMap()).get(name);
        if (info == null) {
            return new PluginDefinitionImpl(getPluginPath(group, name), new byte[]{});
        } else {
            return info.getDefinition();
        }
    }

    @Override
    public Map<String, PluginDefinition> getPluginDefinitions(String type, String group) {
        return getPlugins(type, group).stream().collect(Collectors.toMap(Function.identity(), it ->
                getPluginDefinition(type, group, it)));
    }

    @Override
    public String uploadPlugin(String group, String name, InputStream is, long length) {
        String path = getPluginPath(group, name);
        plugins.computeIfAbsent(group, k -> new HashMap<>())
                .computeIfAbsent(name, k -> {
                    try {
                        return new PluginInfo(new PluginDefinitionImpl(path, PluginStorage.read(is)));
                    } catch (IOException e) {
                        throw new PluginException("Read input stream error", e);
                    }
                });
        return path;
    }

    @Override
    public void loadPlugin(String group, String name) {
        updatePluginInfo(group, name, LOADED);
    }

    @Override
    public void unloadPlugin(String group, String name) {
        updatePluginInfo(group, name, UNLOADED);
    }

    @Override
    public void deletePlugin(String group, String name) {
        updatePluginInfo(group, name, DELETED);
    }

    @Override
    public boolean existPlugin(String group, String name) {
        return plugins.getOrDefault(group, Collections.emptyMap()).containsKey(name);
    }

    @SneakyThrows
    @Override
    public void renamePlugin(String group, String name, String rename) {
        Map<String, PluginInfo> map = plugins.computeIfAbsent(group, k -> new HashMap<>());
        PluginInfo info = map.remove(name);
        if (info == null) {
            return;
        }
        map.put(rename, new PluginInfo(new PluginDefinitionImpl(getPluginPath(group, rename),
                PluginStorage.read(Objects.requireNonNull(info.getDefinition().getInputStream())))));
    }

    protected List<String> getPlugins(String group, String type) {
        return plugins.getOrDefault(group, Collections.emptyMap())
                .values()
                .stream()
                .filter(it -> Objects.equals(it.getType(), type))
                .map(it -> it.getDefinition().getPath())
                .collect(Collectors.toList());
    }

    protected String getPluginPath(String group, String name) {
        return group + "/" + name;
    }

    protected void updatePluginInfo(String group, String name, String type) {
        PluginInfo info = plugins.getOrDefault(group, Collections.emptyMap()).get(name);
        if (info == null) {
            return;
        }
        info.setType(type);
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class PluginInfo {

        private final PluginDefinition definition;

        private String type = UNLOADED;
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
            return new ByteArrayInputStream(bytes);
        }
    }
}
