package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 线程池定时遍历插件文件自动加载
 */
public class DefaultPluginAutoLoader extends AbstractPluginAutoLoader {

    private final Map<String, PluginDefinition> plugins = new LinkedHashMap<>();

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
    protected void onStart(List<? extends PluginDefinition> definitions) {
        syncWrite(() -> plugins.putAll(toMap(definitions)));
    }

    @Override
    protected void onRefresh(List<PluginDefinition> definitions) {
        syncWrite(() -> {
            Map<String, PluginDefinition> load = new LinkedHashMap<>();
            Map<String, PluginDefinition> reload = new LinkedHashMap<>();

            Map<String, PluginDefinition> lastPlugins = new LinkedHashMap<>(plugins);
            Map<String, PluginDefinition> newPlugins = toMap(definitions);

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

            Map<String, PluginDefinition> unload = new LinkedHashMap<>(lastPlugins);

            load.forEach((path, definition) -> {
                try {
                    plugins.put(path, definition);
                    onCreated(definition);
                } catch (Throwable e) {
                    onError(e);
                }
            });
            reload.forEach((path, definition) -> {
                try {
                    plugins.put(path, definition);
                    onModified(definition);
                } catch (Throwable e) {
                    onError(e);
                }
            });
            unload.forEach((path, definition) -> {
                try {
                    plugins.remove(path);
                    onDeleted(definition);
                } catch (Throwable e) {
                    onError(e);
                }
            });
        });
    }

    private Map<String, PluginDefinition> toMap(Collection<? extends PluginDefinition> plugins) {
        Map<String, PluginDefinition> map = new LinkedHashMap<>();
        for (PluginDefinition plugin : plugins) {
            map.put(plugin.getPath(), plugin);
        }
        return map;
    }
}
