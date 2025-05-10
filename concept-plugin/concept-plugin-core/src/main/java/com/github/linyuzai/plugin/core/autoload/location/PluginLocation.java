package com.github.linyuzai.plugin.core.autoload.location;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 插件位置抽象，本地文件和远程文件（后续扩展）
 */
public interface PluginLocation {

    /**
     * 需要加载的插件
     */
    String LOADED = "_loaded";

    /**
     * 不需要加载的插件
     */
    String UNLOADED = "_unloaded";

    /**
     * 删除的插件
     */
    String DELETED = "_deleted";

    /**
     * 获得基础路径
     */
    String getBasePath();

    /**
     * 插件分组，便于管理不同业务的插件
     */
    List<String> getGroups();


    void addGroup(String group);

    /**
     * 获得分组下的需要加载的插件名
     */
    List<String> getLoadedPlugins(String group);

    /**
     * 获得分组下需要加载的插件路径
     */
    String getLoadedPluginPath(String group, String name);

    /**
     * 获得分组下需要加载的插件流
     */
    InputStream getLoadedPluginInputStream(String group, String name) throws IOException;

    /**
     * 获得分组下的不需要加载的插件名
     */
    List<String> getUnloadedPlugins(String group);

    /**
     * 获得分组下不需要加载的插件路径
     */
    String getUnloadedPluginPath(String group, String name);

    /**
     * 获得分组下不需要加载的插件流
     */
    InputStream getUnloadedPluginInputStream(String group, String name) throws IOException;

    /**
     * 获得分组下的删除的插件名
     */
    List<String> getDeletedPlugins(String group);

    /**
     * 获得分组下删除的插件路径
     */
    String getDeletedPluginPath(String group, String name);

    /**
     * 获得分组下删除的插件流
     */
    InputStream getDeletedPluginInputStream(String group, String name) throws IOException;

    /**
     * 获得插件大小
     */
    long getSize(String path);

    /**
     * 获得插件创建时间
     */
    long getCreationTimestamp(String path);

    /**
     * 上传插件
     */
    String upload(String group, String name, InputStream is, long length) throws IOException;

    /**
     * 加载分组下的插件
     */
    void load(String group, String name);

    /**
     * 卸载分组下的插件
     */
    void unload(String group, String name);

    /**
     * 删除分组下的插件
     */
    void delete(String group, String name);

    /**
     * 分组下的插件是否存在
     */
    boolean exist(String group, String name);

    /**
     * 重命名分组下的插件
     */
    void rename(String group, String name, String rename);

    /**
     * 获得版本
     */
    Object getVersion(String path);

    /**
     * 插件过滤器
     */
    interface Filter {

        /**
         * 根据分组和名称过滤
         */
        boolean filter(String group, String name);
    }
}
