package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.event.PluginLoadErrorEvent;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 基于 {@link WatchService} 的插件文件自动加载器
 */
@Getter
@RequiredArgsConstructor
public class WatchServicePluginAutoLoader implements PluginAutoLoader {

    private final PluginConcept concept;

    /**
     * 执行线程池
     */
    private final Executor executor;

    /**
     * 插件位置
     */
    private final PluginLocation location;

    private final Map<String, Object> pathIdMapping = new ConcurrentHashMap<>();

    private final Map<String, Boolean> watchStates = new ConcurrentHashMap<>();

    private WatchService watchService;

    private boolean running = false;

    /**
     * 开始监听
     */
    @Override
    public synchronized void start() {
        //如果已经开始，直接忽略
        if (running) {
            return;
        }
        running = true;

        //如果没有指定执行器，直接新建一个线程
        if (executor == null) {
            Thread thread = new Thread(this::listen);
            thread.setName("concept-plugin-autoload");
            thread.start();
        } else {
            executor.execute(this::listen);
        }

        for (String group : location.getGroups()) {
            //开始就执行一次回调
            notifyOnStart(group);
            addGroup(group);
        }
    }

    @Override
    public void addGroup(String group) {
        final Path path = Paths.get(location.getGroupPath(group));
        try {
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
            watchStates.put(group, true);
        } catch (Throwable e) {
            watchStates.put(group, true);
        }
    }

    private void notifyOnStart(String group) {
        String[] names = location.getPlugins(group);
        for (String name : names) {
            onNotify(StandardWatchEventKinds.ENTRY_CREATE, group, name);
        }
    }

    /**
     * 停止监听
     */
    @Override
    public synchronized void stop() {
        running = false;
        if (executor instanceof ExecutorService) {
            ExecutorService es = (ExecutorService) executor;
            if (!es.isShutdown()) {
                es.shutdown();
            }
        }
    }

    /**
     * 开始监听。
     * 通过 {@link WatchService} 监听目录，当触发文件新增，修改，删除时进行回调。
     */
    @SneakyThrows
    public void listen() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            this.watchService = watchService;
            while (running) {
                try {
                    final WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        String groupPath = key.watchable().toString();
                        String group = location.getGroup(groupPath);
                        if (group == null) {
                            continue;
                        }
                        onNotify(event, group);
                    }
                    key.reset();
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }
        if (watchService != null) {
            watchService.close();
        }
    }

    public void onNotify(WatchEvent<?> event, String group) {
        WatchEvent.Kind<?> kind = event.kind();
        if (kind == StandardWatchEventKinds.OVERFLOW) {
            return;
        }
        String name = event.context().toString();
        onNotify(kind, group, name);
    }

    public void onNotify(WatchEvent.Kind<?> kind, String group, String name) {
        String pluginPath = location.getPluginPath(group, name);
        if (pluginPath == null) {
            return;
        }
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            onFileCreated(pluginPath);
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            onFileModified(pluginPath);
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            onFileDeleted(pluginPath);
        }
    }

    /**
     * 文件创建
     *
     * @param pluginPath 监听到的事件
     */
    public void onFileCreated(String pluginPath) {
        Plugin plugin = load(pluginPath);
        concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin));
    }

    /**
     * 文件修改
     *
     * @param pluginPath 监听到的事件
     */
    public void onFileModified(String pluginPath) {
        Plugin plugin = reload(pluginPath);
        concept.getEventPublisher().publish(new PluginAutoReloadEvent(plugin));
    }

    /**
     * 文件删除
     *
     * @param pluginPath 监听到的事件
     */
    public void onFileDeleted(String pluginPath) {
        Plugin plugin = unload(pluginPath);
        if (plugin != null) {
            concept.getEventPublisher().publish(new PluginAutoUnloadEvent(plugin));
        }
    }

    public void onError(Throwable e) {
        concept.getEventPublisher().publish(new PluginLoadErrorEvent(e));
    }

    public Plugin load(String pluginPath) {
        Plugin plugin = concept.load(pluginPath);
        pathIdMapping.put(pluginPath, plugin.getId());
        return plugin;
    }

    /**
     * 卸载并移除映射关系
     *
     * @param pluginPath 文件路径
     */
    public Plugin unload(String pluginPath) {
        Object id = pathIdMapping.remove(pluginPath);
        if (id == null) {
            return null;
        }
        return concept.unload(id);
    }

    /**
     * 重新加载
     *
     * @param pluginPath 插件源
     */
    public Plugin reload(String pluginPath) {
        unload(pluginPath);
        return load(pluginPath);
    }
}
