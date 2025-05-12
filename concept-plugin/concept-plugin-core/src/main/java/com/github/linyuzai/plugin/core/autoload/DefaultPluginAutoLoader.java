package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 线程池定时遍历插件文件自动加载
 */
public class DefaultPluginAutoLoader extends AbstractPluginAutoLoader {

    private final Map<String, Object> plugins = new HashMap<>();

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
        executor.schedule(this::doListen, period, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onExistPluginLoaded(Collection<String> paths) {
        plugins.putAll(newPlugins(paths));
    }

    private Map<String, Object> newPlugins(Collection<String> paths) {
        Map<String, Object> newPlugins = new HashMap<>();
        for (String path : paths) {
            Object tag = storage.getVersion(path);
            newPlugins.put(path, tag);
        }
        return newPlugins;
    }

    /**
     * 开始监听。
     */
    @SneakyThrows
    public synchronized void doListen() {
        try {
            Map<String, Object> load = new HashMap<>();
            Map<String, Object> reload = new HashMap<>();

            Map<String, Object> newPlugins = newPlugins(getPluginPaths());
            Map<String, Object> lastPlugins = new HashMap<>(plugins);

            for (Map.Entry<String, Object> entry : newPlugins.entrySet()) {
                Object tag = lastPlugins.remove(entry.getKey());
                if (tag == null) {
                    load.put(entry.getKey(), entry.getValue());
                } else {
                    if (Objects.equals(tag, entry.getValue())) {
                        // 相同的文件忽略
                    } else {
                        reload.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            Map<String, Object> unload = new HashMap<>(lastPlugins);

            load.forEach((path, tag) -> {
                try {
                    plugins.put(path, tag);
                    onCreated(path);
                } catch (Throwable e) {
                    onListenError(e);
                }
            });
            reload.forEach((path, tag) -> {
                try {
                    plugins.put(path, tag);
                    onModified(path);
                } catch (Throwable e) {
                    onListenError(e);
                }
            });
            unload.forEach((path, tag) -> {
                try {
                    plugins.remove(path);
                    onDeleted(path);
                } catch (Throwable e) {
                    onListenError(e);
                }
            });
        } catch (Throwable e) {
            onListenError(e);
        }
    }
}
