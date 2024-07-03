package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.repository.PluginRepository;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * 插件概念
 */
public interface PluginConcept {

    void initialize();

    void destroy();

    void addHandlers(PluginHandler... handlers);

    void addHandlers(Collection<? extends PluginHandler> handlers);

    void removeHandlers(PluginHandler... handlers);

    void removeHandlers(Collection<? extends PluginHandler> handlers);

    /**
     * 创建插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin create(Object o, PluginContext context);

    /**
     * 加载插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin load(Object o);

    /**
     * 加载插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin load(Object o, boolean reloadIfExist);

    /**
     * 加载插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin load(Object o, PluginContext context, Consumer<Plugin> onSuccess, Consumer<Throwable> onError, Runnable onComplete);

    /**
     * 卸载插件
     *
     * @param o 插件源
     */
    Plugin unload(Object o);

    boolean isLoaded(Object o);

    boolean isLoading(Object o);

    boolean isUnloading(Object o);

    /**
     * 获得插件存储
     *
     * @return 插件或 null
     */
    PluginRepository getRepository();

    PluginEventPublisher getEventPublisher();

    PluginLogger getLogger();
}
