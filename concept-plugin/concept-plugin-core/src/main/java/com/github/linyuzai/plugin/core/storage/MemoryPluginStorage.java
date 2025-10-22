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
    public List<String> getGroups() {
        return syncRead(() -> new ArrayList<>(plugins.keySet()));
    }

    @Override
    public void addGroup(String group) {
        syncWrite(() -> plugins.computeIfAbsent(group, f -> new LinkedHashMap<>()));
    }

    @Override
    public PluginDefinition getPluginDefinition(String type, String group, String name) {
        return syncRead(() -> {
            PluginDefinition definition = plugins.getOrDefault(group, Collections.emptyMap()).get(name);
            if (definition == null) {
                return new PluginDefinitionImpl(getPluginPath(group, name), name, null);
            } else {
                return definition;
            }
        });
    }

    @Override
    public Stream<PluginDefinition> getPluginDefinitions(String type, String group) {
        return syncRead(() -> plugins.getOrDefault(group, Collections.emptyMap())
                .values()
                .stream()
                .filter(it -> type.equals(types.get(it.getPath()))));
    }

    @Override
    public String uploadPlugin(String group, String name, InputStream is, long length) {
        return syncWrite(() -> {
            String generated = generateName(group, name);
            String path = getPluginPath(group, generated);
            PluginDefinition definition = new PluginDefinitionImpl(path, generated, ReadUtils.read(is));
            types.put(path, UNLOADED);
            plugins.get(group).put(generated, definition);
            return generated;
        });
    }

    @Override
    public void loadPlugin(String group, String name) {
        syncWrite(() -> updatePluginInfo(group, name, LOADED));
    }

    @Override
    public void unloadPlugin(String group, String name) {
        syncWrite(() -> updatePluginInfo(group, name, UNLOADED));
    }

    @Override
    public void deletePlugin(String group, String name) {
        syncWrite(() -> {
            updatePluginInfo(group, name, DELETED);
            renamePlugin(group, name, generateDeletedName(group, name));
        });
    }

    @Override
    public boolean existPlugin(String group, String name) {
        return syncRead(() -> plugins.getOrDefault(group, Collections.emptyMap()).containsKey(name));
    }

    @SneakyThrows
    @Override
    public void renamePlugin(String group, String name, String rename) {
        syncWrite(() -> {
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
        });
    }

    @Override
    public void clearDeleted(String group) {
        syncWrite(() -> {
            Map<String, PluginDefinition> definitions = plugins.getOrDefault(group, Collections.emptyMap());
            Set<String> names = definitions.keySet();
            Iterator<String> iterator = names.iterator();
            while (iterator.hasNext()) {
                String name = iterator.next();
                String path = getPluginPath(group, name);
                String type = types.get(path);
                if (DELETED.equals(type)) {
                    types.remove(path);
                    iterator.remove();
                }
            }
        });
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
        Map<String, PluginDefinition> definitionMap = plugins.get(group);
        if (definitionMap == null) {
            return;
        }
        PluginDefinition definition = definitionMap.get(name);
        if (definition == null) {
            return;
        }
        definitionMap.put(name, new PluginDefinitionImpl(
                definition.getPath(),
                definition.getName(),
                definition.getCreateTime(),
                ReadUtils.read(definition.getInputStream())));
    }

    @Getter
    @RequiredArgsConstructor
    public static class PluginDefinitionImpl implements PluginDefinition {

        private final String path;

        private final String name;

        private final long createTime;

        private final long updateTime;

        private final byte[] bytes;

        public PluginDefinitionImpl(String path, String name, byte[] bytes) {
            this(path, name, System.currentTimeMillis(), bytes);
        }

        public PluginDefinitionImpl(String path, String name, long createTime, byte[] bytes) {
            this.path = path;
            this.name = name;
            this.createTime = createTime;
            this.updateTime = System.currentTimeMillis();
            this.bytes = bytes;
        }

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public Object getVersion() {
            return updateTime;
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
