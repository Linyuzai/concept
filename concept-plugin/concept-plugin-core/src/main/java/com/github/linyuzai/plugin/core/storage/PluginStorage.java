package com.github.linyuzai.plugin.core.storage;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
    InputStream getLoadedPluginInputStream(String group, String name);

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
    InputStream getUnloadedPluginInputStream(String group, String name);

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
    InputStream getDeletedPluginInputStream(String group, String name);

    /**
     * 获得插件源
     */
    PluginDefinition getPluginDefinition(String path);

    /**
     * 获得插件源
     */
    List<PluginDefinition> getPluginDefinitions(Collection<? extends String> paths);

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
     * 生成文件名
     */
    default String generateName(String name,
                                Function<String, Boolean> exist,
                                Function<Integer, String> generator) {
        int i = 1;
        int index = name.lastIndexOf(".");
        String prefix;
        String suffix;
        if (index == -1) {
            prefix = name;
            suffix = "";
        } else {
            prefix = name.substring(0, index);
            suffix = name.substring(index);
        }
        String generate = name;
        while (exist.apply(generate)) {
            generate = prefix + generator.apply(i) + suffix;
            if (Objects.equals(generate, name)) {
                throw new IllegalArgumentException("Generated name '" + name + "' is same as original name");
            }
            i++;
        }
        return generate;
    }

    static byte[] read(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int n;
        while (-1 != (n = is.read(buffer))) {
            os.write(buffer, 0, n);
        }
        return os.toByteArray();
    }

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
