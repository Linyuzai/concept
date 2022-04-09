package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.function.BiConsumer;

/**
 * 插件自动加载通知者
 */
@Getter
@AllArgsConstructor
public abstract class PluginNotifier implements BiConsumer<String, WatchEvent.Kind<?>> {

    protected final PluginConcept concept;

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
            Plugin plugin = load(path);
            concept.publish(new PluginAutoLoadEvent(plugin));
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            Plugin plugin = reload(path);
            concept.publish(new PluginAutoReloadEvent(plugin));
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            Plugin plugin = unload(path);
            if (plugin != null) {
                concept.publish(new PluginAutoUnloadEvent(plugin));
            }
        }
    }

    /**
     * 加载
     *
     * @param o 插件源
     */
    public abstract Plugin load(Object o);

    /**
     * 卸载
     *
     * @param o 插件源
     */
    public abstract Plugin unload(Object o);

    /**
     * 重新加载
     *
     * @param o 插件源
     */
    public Plugin reload(Object o) {
        unload(o);
        return load(o);
    }
}
