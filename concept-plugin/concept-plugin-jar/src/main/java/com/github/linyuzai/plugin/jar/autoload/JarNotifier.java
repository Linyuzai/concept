package com.github.linyuzai.plugin.jar.autoload;

import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import lombok.Getter;

import java.net.URL;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * 监听jar
 */
@Getter
public class JarNotifier implements BiConsumer<String, WatchEvent.Kind<?>> {

    private final JarPluginConcept concept;

    /**
     * 文件路径和 {@link URL} 的映射关系
     */
    private final Map<String, URL> pathUrlMapping = new ConcurrentHashMap<>();

    public JarNotifier(JarPluginConcept concept) {
        this.concept = concept;
    }

    /**
     * 新增时加载插件，
     * 修改时移除之前的类加载器并重新加载，
     * 删除时移除对应的类加载器。
     *
     * @param path 文件路径
     * @param kind 新增或修改或删除
     */
    @Override
    public void accept(String path, WatchEvent.Kind<?> kind) {
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            load(path);
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            unload(path);
            load(path);
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            unload(path);
        }
    }

    /**
     * 加载并添加映射关系
     *
     * @param path 文件路径
     */
    public void load(String path) {
        JarPlugin plugin = (JarPlugin) concept.create(path);
        concept.load(plugin);
        pathUrlMapping.put(path, plugin.getUrl());
    }

    /**
     * 卸载并移除映射关系和类加载器
     *
     * @param path 文件路径
     */
    public void unload(String path) {
        URL url = pathUrlMapping.remove(path);
        if (url == null) {
            return;
        }
        concept.getPluginClassLoaders().remove(url);
    }
}
