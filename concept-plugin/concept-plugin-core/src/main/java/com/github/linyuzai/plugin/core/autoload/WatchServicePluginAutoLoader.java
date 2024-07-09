package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.event.PluginLoadErrorEvent;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    private final PluginExecutor executor;

    /**
     * 插件位置
     */
    private final PluginLocation location;

    private final Map<String, Boolean> watchStates = new ConcurrentHashMap<>();

    private WatchService watchService;

    private boolean running = false;

    @Override
    public synchronized void start() {
        start(true);
    }

    /**
     * 开始监听
     */
    @Override
    public synchronized void start(boolean load) {
        //如果已经开始，直接忽略
        if (running) {
            return;
        }
        running = true;

        executor.execute(this::listen);

        String[] groups = location.getGroups();
        for (String group : groups) {
            addGroup(group);
        }

        if (load) {
            List<String> paths = new ArrayList<>();
            for (String group : groups) {
                String[] names = location.getLoadedPlugins(group);
                for (String name : names) {
                    String path = location.getLoadedPluginPath(group, name);
                    if (path == null) {
                        continue;
                    }
                    paths.add(path);
                }
            }

            concept.load(paths, (o, plugin) -> {
                String path = (String) o;
                concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin, path));
            }, (o, e) -> {
                String path = (String) o;
                concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(path, e));
            });
        }
    }

    @Override
    public void addGroup(String group) {
        try {
            String path = location.getLoadedPath(group);
            registerPath(path);
            watchStates.put(group, true);
        } catch (Throwable e) {
            watchStates.put(group, true);
        }
    }

    protected void registerPath(String path) throws IOException {
        Paths.get(path).register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
    }

    @Override
    public Boolean getGroupState(String group) {
        return watchStates.getOrDefault(group, false);
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
        load(path);
    }

    /**
     * 文件修改
     *
     * @param path 监听到的事件
     */
    public void onFileModified(String path) {
        reload(path);
    }

    /**
     * 文件删除
     *
     * @param path 监听到的事件
     */
    public void onFileDeleted(String path) {
        unload(path);
    }

    public void onError(Throwable e) {
        concept.getEventPublisher().publish(new PluginLoadErrorEvent(e));
    }

    public void load(String path) {
        try {
            Plugin plugin = concept.load(path);
            concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin, path));
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(path, e));
        }
    }

    public void reload(String path) {
        unload(path);
        load(path);
        /*try {
            Plugin plugin = concept.load(path, true);
            concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin, path));
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(path, e));
        }*/
    }

    /**
     * 卸载并移除映射关系
     *
     * @param path 文件路径
     */
    public void unload(String path) {
        try {
            Plugin plugin = concept.unload(path);
            if (plugin != null) {
                concept.getEventPublisher().publish(new PluginAutoUnloadEvent(plugin, path));
            }
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new PluginAutoUnloadErrorEvent(path, e));
        }
    }
}
