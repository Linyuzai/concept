package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.event.PluginLoadErrorEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
        final Path path = Paths.get(location.getLoadedPath(group));
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

    @Override
    public Boolean getGroupState(String group) {
        return watchStates.getOrDefault(group, false);
    }

    private void notifyOnStart(String group) {
        String[] names = location.getLoadedPlugins(group);
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
        String path = location.getLoadedPluginPath(group, name);
        if (path == null) {
            return;
        }
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            onFileCreated(path);
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            onFileModified(path);
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            onFileDeleted(path);
        }
    }

    /**
     * 文件创建
     *
     * @param path 监听到的事件
     */
    public void onFileCreated(String path) {
        Plugin plugin = load(path);
        concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin, path));
    }

    /**
     * 文件修改
     *
     * @param path 监听到的事件
     */
    public void onFileModified(String path) {
        Plugin plugin = reload(path);
        concept.getEventPublisher().publish(new PluginAutoReloadEvent(plugin, path));
    }

    /**
     * 文件删除
     *
     * @param path 监听到的事件
     */
    public void onFileDeleted(String path) {
        Plugin plugin = unload(path);
        if (plugin != null) {
            concept.getEventPublisher().publish(new PluginAutoUnloadEvent(plugin, path));
        }
    }

    public void onError(Throwable e) {
        concept.getEventPublisher().publish(new PluginLoadErrorEvent(e));
    }

    public Plugin load(String path) {
        return concept.load(path);
    }

    /**
     * 卸载并移除映射关系
     *
     * @param path 文件路径
     */
    public Plugin unload(String path) {
        return concept.unload(path);
    }

    /**
     * 重新加载
     *
     * @param path 插件源
     */
    public Plugin reload(String path) {
        unload(path);
        return load(path);
    }
}
