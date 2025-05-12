package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.event.PluginConceptInitializedEvent;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件自动加载器抽象类
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractPluginAutoLoader implements PluginAutoLoader {

    protected final PluginConcept concept;

    /**
     * 执行线程池
     */
    protected final PluginExecutor executor;

    /**
     * 插件位置
     */
    protected final PluginStorage storage;

    /**
     * 是否运行
     */
    protected boolean running = false;

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

        concept.getEventPublisher().register(new PluginEventListener() {
            @Override
            public void onEvent(Object event) {
                if (event instanceof PluginConceptInitializedEvent) {
                    concept.getEventPublisher().unregister(this);
                    if (load) {
                        //加载已经存在的插件
                        onExistPluginLoaded(loadPlugins());
                    }
                    listen(executor);
                }
            }
        });
    }

    protected void onExistPluginLoaded(Collection<String> paths) {

    }

    protected Collection<String> loadPlugins() {
        Collection<String> paths = getPluginPaths();
        List<Object> sources = paths.stream()
                .map(storage::getPluginSource)
                .collect(Collectors.toList());
        concept.load(sources, (o, plugin) -> {
            String path = (String) o;
            concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin, path));
        }, (o, e) -> {
            String path = (String) o;
            concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(path, e));
        });
        return paths;
    }

    protected Collection<String> getPluginPaths() {
        List<String> groups = storage.getGroups();
        List<String> paths = new ArrayList<>();
        for (String group : groups) {
            List<String> names = storage.getLoadedPlugins(group);
            for (String name : names) {
                String path = storage.getLoadedPluginPath(group, name);
                if (path == null) {
                    continue;
                }
                paths.add(path);
            }
        }
        return paths;
    }

    protected abstract void listen(PluginExecutor executor);

    @Override
    public void addGroup(String group) {
        this.storage.addGroup(group);
    }

    @Override
    public Boolean getGroupState(String group) {
        return true;
    }

    /**
     * 停止监听
     */
    @Override
    public synchronized void stop() {
        running = false;
    }

    /**
     * 文件创建
     */
    protected void onCreated(String path) {
        load(path);
    }

    /**
     * 文件修改
     */
    protected void onModified(String path) {
        reload(path);
    }

    /**
     * 文件删除
     */
    protected void onDeleted(String path) {
        unload(path);
    }

    protected void onListenError(Throwable e) {
        concept.getLogger().error("Plugin listen error", e);
    }

    /**
     * 加载插件
     */
    public void load(String path) {
        try {
            Object source = storage.getPluginSource(path);
            Plugin plugin = concept.load(source);
            concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin, path));
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(path, e));
        }
    }

    /**
     * 重新加载插件
     */
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
     * 卸载插件
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
