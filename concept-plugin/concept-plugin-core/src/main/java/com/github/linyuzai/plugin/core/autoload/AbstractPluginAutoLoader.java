package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.event.PluginConceptInitializedEvent;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.sync.SyncSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 插件自动加载器抽象类
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractPluginAutoLoader extends SyncSupport implements PluginAutoLoader {

    protected final PluginConcept concept;

    protected final PluginExecutor executor;

    protected final PluginStorage storage;

    /**
     * 是否运行
     */
    protected volatile boolean running = false;

    /**
     * 开始监听
     */
    @Override
    public void start() {
        syncWrite(() -> {
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
                        //加载已经存在的插件
                        onStart(loadPlugins());
                        listen(executor);
                    }
                }
            });
        });
    }

    /**
     * 初始化插件加载回调
     *
     * @param definitions 初始化加载的插件
     */
    protected void onStart(List<? extends PluginDefinition> definitions) {

    }

    /**
     * 初始化加载插件
     *
     * @return 加载的插件
     */
    protected List<PluginDefinition> loadPlugins() {
        List<PluginDefinition> definitions = getPluginDefinitions();
        concept.load(definitions,
                (definition, plugin) ->
                        concept.getEventPublisher().publish(new PluginAutoLoadEvent(plugin)),
                (definition, e) ->
                        concept.getEventPublisher().publish(new PluginAutoLoadErrorEvent(definition, e)));
        return definitions;
    }

    /**
     * 触发插件加载或卸载
     */
    @Override
    public void refresh() {
        try {
            onRefresh(getPluginDefinitions());
        } catch (Throwable e) {
            onError(e);
        }
    }

    /**
     * 获得需要加载的插件
     *
     * @return 需要加载的插件
     */
    protected List<PluginDefinition> getPluginDefinitions() {
        return storage.getGroups()
                .stream()
                .flatMap(it -> storage.getPluginDefinitions(PluginStorage.LOADED, it))
                .collect(Collectors.toList());
    }

    /**
     * 监听插件刷新
     */
    protected void listen(PluginExecutor executor) {
        long period = getPeriod();
        if (period > 0) {
            executor.schedule(this::refresh, period, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 返回刷新间隔
     *
     * @return 刷新间隔
     */
    protected abstract long getPeriod();

    /**
     * 刷新插件加载或卸载
     *
     * @param definitions 需要加载的插件
     */
    protected abstract void onRefresh(List<PluginDefinition> definitions);

    /**
     * 停止监听
     */
    @Override
    public void stop() {
        syncWrite(() -> running = false);
    }

    /**
     * 异常时回调
     *
     * @param e 异常
     */
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
