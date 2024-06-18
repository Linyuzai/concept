package com.github.linyuzai.plugin.core.autoload.location;

import java.io.File;

public interface PluginLocation {

    /**
     * 默认的缓存路径。
     */
    String DEFAULT_BASE_PATH = new File(System.getProperty("user.home"), "concept/plugin").getAbsolutePath();

    String getBasePath();

    String[] getGroups();

    String getGroup(String path);

    String getLoadedPath(String group);

    String[] getLoadedPlugins(String group);

    String getLoadedPluginPath(String group, String name);

    String getUnloadedPath(String group);

    String[] getUnloadedPlugins(String group);

    String getUnloadedPluginPath(String group, String name);

    String getDeletedPath(String group);

    String[] getDeletedPlugins(String group);

    String getDeletedPluginPath(String group, String name);

    long getSize(String path);

    long getCreationTimestamp(String path);

    void load(String group, String name);

    void unload(String group, String name);

    void delete(String group, String name);

    interface Filter {

        boolean filter(String group, String name);
    }
}
