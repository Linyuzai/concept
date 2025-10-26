package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 线程池定时遍历插件自动加载
 */
public class DefaultPluginAutoLoader extends AbstractPluginAutoLoader {

    /**
     * 当前已加载插件
     */
    private final Map<String, PluginDefinition> plugins = new LinkedHashMap<>();

    /**
     * 刷新间隔
     */
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

    /**
     * 初始化插件加载后更新当前加载插件信息
     *
     * @param definitions 初始化加载的插件
     */
    @Override
    protected void onStart(List<? extends PluginDefinition> definitions) {
        syncWrite(() -> plugins.putAll(toMap(definitions)));
    }

    /**
     * 比对当前加载的插件和最新需要加载的插件，
     * 根据比对的结果加载卸载重新加载插件
     *
     * @param definitions 需要加载的插件
     */
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
                    load(definition);
                } catch (Throwable e) {
                    onError(e);
                }
            });
            reload.forEach((path, definition) -> {
                try {
                    plugins.put(path, definition);
                    reload(definition);
                } catch (Throwable e) {
                    onError(e);
                }
            });
            unload.forEach((path, definition) -> {
                try {
                    plugins.remove(path);
                    unload(definition);
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
