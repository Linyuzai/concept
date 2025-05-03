package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于目录监听的插件文件自动加载
 *
 * @see WatchService
 */
// Linux 会有监听问题
@Deprecated
public class WatchServicePluginAutoLoader extends AbstractPluginAutoLoader {

    /**
     * 目录状态
     */
    private final Map<String, Boolean> watchStates = new ConcurrentHashMap<>();

    private WatchService watchService;

    public WatchServicePluginAutoLoader(PluginConcept concept, PluginExecutor executor, PluginLocation location) {
        super(concept, executor, location);
    }

    @Override
    protected void listen(PluginExecutor executor) {
        executor.execute(this::doListen);
    }

    @Override
    public void addGroup(String group) {
        try {
            String path = location.getLoadedBasePath(group);
            registerPath(path, watchService);
            watchStates.put(group, true);
        } catch (Throwable e) {
            watchStates.put(group, false);
        }
    }

    protected void registerPath(String path, WatchService watchService) throws IOException {
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
     * 开始监听。
     * 通过 {@link WatchService} 监听目录，当触发文件新增，修改，删除时进行回调。
     */
    @SneakyThrows
    public void doListen() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            this.watchService = watchService;
            //添加目录监听
            String[] groups = location.getGroups();
            for (String group : groups) {
                addGroup(group);
            }
            while (running) {
                try {
                    final WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        //获得分组
                        String groupPath = key.watchable().toString();
                        String group = location.getGroup(groupPath);
                        if (group == null) {
                            continue;
                        }
                        onNotify(event, group);
                    }
                    key.reset();
                } catch (Throwable e) {
                    onListenError(e);
                }
            }
        } catch (Throwable e) {
            onListenError(e);
        }
    }

    public void onNotify(WatchEvent<?> event, String group) {
        WatchEvent.Kind<?> kind = event.kind();
        if (kind == StandardWatchEventKinds.OVERFLOW) {
            return;
        }
        String name = event.context().toString();//文件名
        onNotify(kind, group, name);
    }

    public void onNotify(WatchEvent.Kind<?> kind, String group, String name) {
        String path = location.getLoadedPluginPath(group, name);
        if (path == null) {
            return;
        }
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            onCreated(path);
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            onModified(path);
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            onDeleted(path);
        }
    }
}
