package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.event.PluginConceptInitializedEvent;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
                        loadedOnStart(loadPlugins());
                    }
                    listen(executor);
                }
            }
        });
    }

    protected void loadedOnStart(Collection<? extends PluginDefinition> definitions) {

    }

    protected Collection<PluginDefinition> loadPlugins() {
        return concept.load(getPluginPaths(),
                (path, plugin) -> concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin, path)),
                (path, e) -> concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(path, e)));
    }

    protected String getPath(Object source) {
        if (source instanceof Plugin) {
            return getPath(((Plugin) source).getDefinition());
        } else if (source instanceof String) {
            return (String) source;
        } else if (source instanceof PluginDefinition) {
            return ((PluginDefinition) source).getPath();
        } else {
            throw new IllegalArgumentException("Can not get path: " + source);
        }
    }

    protected List<String> getPluginPaths() {
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
    protected void onCreated(String path, PluginDefinition definition) {
        load(path, definition);
    }

    /**
     * 文件修改
     */
    protected void onModified(String path, PluginDefinition definition) {
        reload(path, definition);
    }

    /**
     * 文件删除
     */
    protected void onDeleted(String path, PluginDefinition definition) {
        unload(path, definition);
    }

    protected void onError(Throwable e) {
        concept.getLogger().error("Plugin autoload error", e);
    }

    /**
     * 加载插件
     */
    public void load(String path, PluginDefinition definition) {
        try {
            Plugin plugin = concept.load(definition);
            concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin, path));
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(path, e));
        }
    }

    /**
     * 重新加载插件
     */
    public void reload(String path, PluginDefinition definition) {
        unload(path, definition);
        load(path, definition);
    }

    /**
     * 卸载插件
     */
    public void unload(String path, PluginDefinition definition) {
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
