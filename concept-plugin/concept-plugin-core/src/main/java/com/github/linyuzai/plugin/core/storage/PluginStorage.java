package com.github.linyuzai.plugin.core.storage;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * 插件存储抽象，本地文件或远程文件
 */
public interface PluginStorage {

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

    void initialize();

    /**
     * 获得插件位置
     */
    String getLocation();

    /**
     * 插件分组，便于管理不同业务的插件
     */
    List<String> getGroups();

    /**
     * 添加分组
     */
    void addGroup(String group);

    /**
     * 获得插件源
     */
    PluginDefinition getPluginDefinition(String type, String group, String name);

    /**
     * 获得插件源
     */
    Stream<PluginDefinition> getPluginDefinitions(String type, String group);

    /**
     * 上传插件
     */
    String uploadPlugin(String group, String name, InputStream is, long length);

    /**
     * 加载分组下的插件
     */
    void loadPlugin(String group, String name);

    /**
     * 卸载分组下的插件
     */
    void unloadPlugin(String group, String name);

    /**
     * 删除分组下的插件
     */
    void deletePlugin(String group, String name);

    /**
     * 分组下的插件是否存在
     */
    boolean existPlugin(String group, String name);

    /**
     * 重命名分组下的插件
     */
    void renamePlugin(String group, String name, String rename);

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
