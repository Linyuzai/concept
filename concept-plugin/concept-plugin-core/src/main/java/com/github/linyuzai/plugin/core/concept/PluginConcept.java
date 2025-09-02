package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 插件概念，统一功能入口
 */
public interface PluginConcept {

    /**
     * 初始化
     */
    void initialize();

    /**
     * 销毁
     */
    void destroy();

    /**
     * 添加插件处理器
     */
    void addHandlers(PluginHandler... handlers);

    /**
     * 添加插件处理器
     */
    void addHandlers(Collection<? extends PluginHandler> handlers);

    /**
     * 移除插件处理器
     */
    void removeHandlers(PluginHandler... handlers);

    /**
     * 移除插件处理器
     */
    void removeHandlers(Collection<? extends PluginHandler> handlers);

    /**
     * 创建上下文
     */
    PluginContext createContext();

    /**
     * 获得插件配置
     */
    PluginMetadata createMetadata(PluginDefinition definition, PluginContext context);

    /**
     * 创建插件
     */
    Plugin createPlugin(PluginDefinition definition, PluginContext context);

    /**
     * 加载插件
     */
    Plugin load(String path);

    /**
     * 加载插件
     */
    Plugin load(PluginDefinition definition);

    /**
     * 加载插件
     */
    Collection<PluginDefinition> load(Collection<String> paths,
                                      BiConsumer<String, Plugin> onSuccess,
                                      BiConsumer<String, Throwable> onError);

    /**
     * 卸载插件
     */
    Plugin unload(String path);

    /**
     * 插件是否正在加载
     */
    boolean isLoading(String path);

    /**
     * 插件是否正在卸载
     */
    boolean isUnloading(String path);

    /**
     * 插件是否已加载
     */
    boolean isLoaded(String path);

    /**
     * 获得上下文工厂
     */
    PluginContextFactory getContextFactory();

    /**
     * 获得插件树工厂
     */
    PluginTreeFactory getTreeFactory();

    /**
     * 获得插件处理链工厂
     */
    PluginHandlerChainFactory getHandlerChainFactory();

    /**
     * 获得插件存储
     */
    PluginRepository getRepository();

    /**
     * 获得事件发布器
     */
    PluginEventPublisher getEventPublisher();

    /**
     * 获得日志
     */
    PluginLogger getLogger();
}
