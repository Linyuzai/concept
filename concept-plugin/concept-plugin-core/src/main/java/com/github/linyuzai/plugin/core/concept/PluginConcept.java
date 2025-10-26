package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandlerFactory;
import com.github.linyuzai.plugin.core.intercept.PluginInterceptor;
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import com.github.linyuzai.plugin.core.path.PluginPathFactory;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
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
     * 添加插件拦截器
     */
    void addInterceptor(PluginInterceptor... interceptors);

    /**
     * 添加插件拦截器
     */
    void addInterceptor(Collection<? extends PluginInterceptor> interceptors);

    /**
     * 移除插件拦截器
     */
    void removeInterceptor(PluginInterceptor... interceptors);

    /**
     * 移除插件拦截器
     */
    void removeInterceptor(Collection<? extends PluginInterceptor> interceptors);

    /**
     * 创建上下文
     */
    PluginContext createContext();

    /**
     * 创建插件元数据
     */
    PluginMetadata createMetadata(PluginDefinition definition, PluginContext context);

    /**
     * 创建插件
     */
    Plugin createPlugin(PluginDefinition definition, PluginContext context);

    /**
     * 加载插件
     */
    Plugin load(PluginDefinition definition);

    /**
     * 加载插件
     */
    void load(Collection<? extends PluginDefinition> paths,
              BiConsumer<PluginDefinition, Plugin> onSuccess,
              BiConsumer<PluginDefinition, Throwable> onError);

    /**
     * 卸载插件
     */
    Plugin unload(PluginDefinition definition);

    /**
     * 插件是否已加载
     */
    boolean isLoaded(PluginDefinition definition);

    /**
     * 插件是否正在加载
     */
    boolean isLoading(PluginDefinition definition);

    /**
     * 插件是否正在卸载
     */
    boolean isUnloading(PluginDefinition definition);

    /**
     * 获得路径工厂
     */
    PluginPathFactory getPathFactory();

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
    PluginStorage getStorage();

    /**
     * 获得插件仓储
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

    /**
     * 获得元数据工厂
     */
    List<PluginMetadataFactory> getMetadataFactories();

    /**
     * 获得插件工厂
     */
    List<PluginFactory> getFactories();

    /**
     * 获得处理器
     */
    List<PluginHandler> getHandlers();

    /**
     * 获得处理器工厂
     */
    List<PluginHandlerFactory> getHandlerFactories();

    /**
     * 获得拦截器
     */
    List<PluginInterceptor> getInterceptors();
}
