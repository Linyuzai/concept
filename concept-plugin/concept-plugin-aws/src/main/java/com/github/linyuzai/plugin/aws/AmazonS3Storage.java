package com.github.linyuzai.plugin.aws;

import com.github.linyuzai.plugin.core.storage.PluginStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public abstract class AmazonS3Storage implements PluginStorage {

    public static final String METADATA_STATUS = "ConceptPlugin.Status";

    public static final String METADATA_CREATION = "ConceptPlugin.Creation";

    protected final String bucket;

    @Override
    public String getLocation() {
        return bucket;
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
        return getPluginInputStream(group, name);
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
        return getPluginInputStream(group, name);
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
        return getPluginInputStream(group, name);
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
        updateStatus(group, name, PluginStorage.LOADED);
    }

    @Override
    public void unloadPlugin(String group, String name) {
        updateStatus(group, name, PluginStorage.UNLOADED);
    }

    @Override
    public void deletePlugin(String group, String name) {
        updateStatus(group, name, PluginStorage.DELETED);
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

    protected abstract String validBucket();

    protected abstract List<String> getPlugins(String group, String type);

    protected abstract InputStream getPluginInputStream(String group, String name) throws IOException;

    protected abstract void updateStatus(String group, String name, String status);

    protected abstract void putObject(String bucket, String key,
                                      InputStream is, long length,
                                      Map<String, String> userMetadata);
}
