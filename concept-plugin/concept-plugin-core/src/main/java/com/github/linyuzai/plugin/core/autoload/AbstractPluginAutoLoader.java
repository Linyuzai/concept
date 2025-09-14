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
import java.util.Map;

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
        List<PluginDefinition> definitions = getPluginDefinitions();
        concept.load(definitions,
                (definition, plugin) ->
                        concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin)),
                (definition, e) ->
                        concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(definition, e)));
        return definitions;
    }

    protected List<PluginDefinition> getPluginDefinitions() {
        List<String> groups = storage.getGroups();
        List<PluginDefinition> definitions = new ArrayList<>();
        for (String group : groups) {
            Map<String, PluginDefinition> map = storage.getPluginDefinitions(PluginStorage.LOADED, group);
            definitions.addAll(map.values());
        }
        return definitions;
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
    protected void onCreated(PluginDefinition definition) {
        load(definition);
    }

    /**
     * 文件修改
     */
    protected void onModified(PluginDefinition definition) {
        reload(definition);
    }

    /**
     * 文件删除
     */
    protected void onDeleted(PluginDefinition definition) {
        unload(definition);
    }

    protected void onError(Throwable e) {
        concept.getLogger().error("Plugin autoload error", e);
    }

    /**
     * 加载插件
     */
    public void load(PluginDefinition definition) {
        try {
            Plugin plugin = concept.load(definition);
            concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin));
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(definition, e));
        }
    }

    /**
     * 重新加载插件
     */
    public void reload(PluginDefinition definition) {
        unload(definition);
        load(definition);
    }

    /**
     * 卸载插件
     */
    public void unload(PluginDefinition definition) {
        try {
            Plugin plugin = concept.unload(definition);
            if (plugin != null) {
                concept.getEventPublisher().publish(new PluginAutoUnloadEvent(plugin));
            }
        } catch (Throwable e) {
            concept.getEventPublisher().publish(new PluginAutoUnloadErrorEvent(definition, e));
        }
    }
}
