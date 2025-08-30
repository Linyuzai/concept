package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.storage.PluginDefinition;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 线程池定时遍历插件文件自动加载
 */
public class DefaultPluginAutoLoader extends AbstractPluginAutoLoader {

    private final Map<String, PluginDefinition> plugins = new HashMap<>();

    @Getter
    @Setter
    private long period;

    public DefaultPluginAutoLoader(PluginConcept concept,
                                   PluginExecutor executor,
                                   PluginStorage storage,
                                   long period) {
        super(concept, executor, storage);
        this.period = period;
    }

    @Override
    protected void listen(PluginExecutor executor) {
        if (period > 0) {
            executor.schedule(this::refresh, period, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected synchronized void loadedOnStart(List<PluginDefinition> definitions) {
        plugins.putAll(listToMap(definitions));
    }

    /**
     * 刷新插件。
     */
    @Override
    public void refresh() {
        try {
            doRefresh(storage.getPluginDefinitions(getPluginPaths()));
        } catch (Throwable e) {
            onError(e);
        }
    }

    protected synchronized void doRefresh(List<PluginDefinition> definitions) {
        Map<String, PluginDefinition> load = new HashMap<>();
        Map<String, PluginDefinition> reload = new HashMap<>();

        Map<String, PluginDefinition> lastPlugins = new HashMap<>(plugins);
        Map<String, PluginDefinition> newPlugins = listToMap(definitions);

        for (Map.Entry<String, PluginDefinition> entry : newPlugins.entrySet()) {
            PluginDefinition last = lastPlugins.remove(entry.getKey());
            if (last == null) {
                load.put(entry.getKey(), entry.getValue());
            } else {
                if (!Objects.equals(last.getVersion(),
                        entry.getValue().getVersion())) {
                    reload.put(entry.getKey(), entry.getValue());
                }
            }
        }

        Map<String, PluginDefinition> unload = new HashMap<>(lastPlugins);

        load.forEach((path, source) -> {
            try {
                plugins.put(path, source);
                onCreated(path, source);
            } catch (Throwable e) {
                onError(e);
            }
        });
        reload.forEach((path, source) -> {
            try {
                plugins.put(path, source);
                onModified(path, source);
            } catch (Throwable e) {
                onError(e);
            }
        });
        unload.forEach((path, source) -> {
            try {
                plugins.remove(path);
                onDeleted(path, source);
            } catch (Throwable e) {
                onError(e);
            }
        });
    }

    private Map<String, PluginDefinition> listToMap(List<PluginDefinition> plugins) {
        Map<String, PluginDefinition> map = new HashMap<>();
        for (PluginDefinition plugin : plugins) {
            map.put(plugin.getPath(), plugin);
        }
        return map;
    }
}
