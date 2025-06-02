package com.github.linyuzai.plugin.aws;

import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.factory.PluginDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class AbstractAwsStorage implements PluginStorage {

    public static final String METADATA_STATUS = "ConceptPlugin.Status";

    public static final String METADATA_CREATION = "ConceptPlugin.Creation";

    protected final String bucket;

    @Setter
    protected Executor executor;

    @Override
    public String getLocation() {
        return bucket;
    }

    @Override
    public List<String> getGroups() {
        return listObjects(getOrCreateBucket(), null, "/")
                .stream()
                .map(it -> {
                    if (it.endsWith("/")) {
                        return it.substring(0, it.length() - 1);
                    } else {
                        return it;
                    }
                })
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public void addGroup(String group) {
        Properties properties = new Properties();
        properties.setProperty("group.name", group);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        properties.store(os, "");
        byte[] bytes = os.toByteArray();
        putObject(bucket, getPluginPath(group, "concept_plugin.properties"),
                new ByteArrayInputStream(bytes), bytes.length, Collections.emptyMap());
    }

    @Override
    public List<String> getLoadedPlugins(String group) {
        return getPlugins(group, PluginStorage.LOADED);
    }

    @Override
    public String getLoadedPluginPath(String group, String name) {
        return getPluginPath(group, name);
    }

    @Override
    public InputStream getLoadedPluginInputStream(String group, String name) throws IOException {
        return getObject(getOrCreateBucket(), getPluginPath(group, name));
    }

    @Override
    public List<String> getUnloadedPlugins(String group) {
        return getPlugins(group, PluginStorage.UNLOADED);
    }

    @Override
    public String getUnloadedPluginPath(String group, String name) {
        return getPluginPath(group, name);
    }

    @Override
    public InputStream getUnloadedPluginInputStream(String group, String name) throws IOException {
        return getObject(getOrCreateBucket(), getPluginPath(group, name));
    }

    @Override
    public List<String> getDeletedPlugins(String group) {
        return getPlugins(group, PluginStorage.DELETED);
    }

    @Override
    public String getDeletedPluginPath(String group, String name) {
        return getPluginPath(group, PluginStorage.DELETED);
    }

    @Override
    public InputStream getDeletedPluginInputStream(String group, String name) throws IOException {
        return getObject(getOrCreateBucket(), getPluginPath(group, name));
    }

    @Override
    public List<PluginDefinition> getPluginDefinitions(Collection<? extends String> paths) {
        List<CompletableFuture<PluginDefinition>> futures = new ArrayList<>();
        for (String path : paths) {
            Supplier<PluginDefinition> supplier = () -> getPluginDefinition(path);
            CompletableFuture<PluginDefinition> future;
            if (executor == null) {
                future = CompletableFuture.supplyAsync(supplier);
            } else {
                future = CompletableFuture.supplyAsync(supplier, executor);
            }
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return futures.stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @Override
    public String uploadPlugin(String group, String name, InputStream is, long length) {
        String pluginName = getPluginName(group, name);
        Map<String, String> map = new LinkedHashMap<>();
        map.put(METADATA_STATUS, PluginStorage.UNLOADED);
        map.put(METADATA_CREATION, String.valueOf(new Date().getTime()));
        putObject(bucket, getPluginPath(group, pluginName), is, length, map);
        return pluginName;
    }

    @Override
    public void loadPlugin(String group, String name) {
        Map<String, String> userMetadata = createUserMetadata(group, name, PluginStorage.LOADED);
        putUserMetadata(group, name, userMetadata);
    }

    @Override
    public void unloadPlugin(String group, String name) {
        Map<String, String> userMetadata = createUserMetadata(group, name, PluginStorage.UNLOADED);
        putUserMetadata(group, name, userMetadata);
    }

    @Override
    public void deletePlugin(String group, String name) {
        Map<String, String> userMetadata = createUserMetadata(group, name, PluginStorage.DELETED);
        putUserMetadata(group, name, userMetadata);
    }

    protected String getPluginPath(String group, String name) {
        return group + "/" + name;
    }

    protected String getPluginName(String group, String name) {
        int i = 1;
        String tryName = name;
        while (existPlugin(group, tryName)) {
            int index = tryName.lastIndexOf(".");
            if (index == -1) {
                tryName = tryName + i;
            } else {
                tryName = tryName.substring(0, index) + "(" + i + ")" + tryName.substring(index);
            }
            i++;
        }
        return tryName;
    }

    protected String getOrCreateBucket() {
        if (!existBucket(bucket)) {
            createBucket(bucket);
        }
        return bucket;
    }

    protected Map<String, String> createUserMetadata(String group, String name, String status) {
        Map<String, String> userMetadata = getUserMetadata(getOrCreateBucket(), getPluginPath(group, name));
        Map<String, String> newUserMetadata;
        if (userMetadata == null) {
            newUserMetadata = new LinkedHashMap<>();
            newUserMetadata.put(METADATA_CREATION, String.valueOf(new Date().getTime()));
        } else {
            newUserMetadata = userMetadata;
        }
        newUserMetadata.put(METADATA_STATUS, status);
        return newUserMetadata;
    }

    protected List<String> getPlugins(String group, String type) {
        String bucketToUse = getOrCreateBucket();
        List<String> list = listObjects(bucketToUse, group + "/", "/");
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (String plugin : list) {
            Supplier<String> supplier = () -> {
                Map<String, String> userMetadata = getUserMetadata(bucketToUse, plugin);
                String pluginStatus = userMetadata.get(METADATA_STATUS);
                if (type.equals(pluginStatus)) {
                    return plugin;
                } else {
                    return null;
                }
            };
            CompletableFuture<String> future;
            if (executor == null) {
                future = CompletableFuture.supplyAsync(supplier);
            } else {
                future = CompletableFuture.supplyAsync(supplier, executor);
            }
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return futures.stream().map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected abstract boolean existBucket(String bucket);

    protected abstract void createBucket(String bucket);

    protected abstract List<String> listObjects(String bucket, String prefix, String delimiter);

    protected abstract Map<String, String> getUserMetadata(String bucket, String key);

    protected abstract void putUserMetadata(String bucket, String key, Map<String, String> userMetadata);

    protected abstract InputStream getObject(String bucket, String key) throws IOException;

    protected abstract void putObject(String bucket, String key,
                                      InputStream is, long length,
                                      Map<String, String> userMetadata);
}
