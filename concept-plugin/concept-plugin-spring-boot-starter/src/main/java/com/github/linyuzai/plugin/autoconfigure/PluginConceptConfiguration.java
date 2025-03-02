package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.autoload.ConditionalOnPluginAutoloadEnabled;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanResolver;
import com.github.linyuzai.plugin.autoconfigure.event.ApplicationConnectionEventPublisher;
import com.github.linyuzai.plugin.autoconfigure.factory.BinderMetadataJarPluginFactory;
import com.github.linyuzai.plugin.autoconfigure.logger.CommonsPluginLogger;
import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.autoconfigure.processor.DynamicPluginProcessor;
import com.github.linyuzai.plugin.core.autoload.DefaultPluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.location.LocalPluginLocation;
import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.DefaultPluginConcept;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.DefaultPluginContextFactory;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.executer.DefaultPluginExecutor;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.handle.DefaultPluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandlerFactory;
import com.github.linyuzai.plugin.core.handle.extract.*;
import com.github.linyuzai.plugin.core.handle.resolve.ContentResolver;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;
import com.github.linyuzai.plugin.core.handle.resolve.PropertiesResolver;
import com.github.linyuzai.plugin.core.logger.PluginErrorLogger;
import com.github.linyuzai.plugin.core.logger.PluginStandardLogger;
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.repository.DefaultPluginRepository;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.tree.DefaultPluginTreeFactory;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import com.github.linyuzai.plugin.jar.autoload.JarLocationFilter;
import com.github.linyuzai.plugin.jar.factory.JarSubPluginFactory;
import com.github.linyuzai.plugin.jar.handle.extract.ClassExtractor;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * 插件配置类
 */
@Configuration(proxyBeanMethods = false)
public class PluginConceptConfiguration {

    @Bean
    public static DynamicPluginProcessor dynamicPluginProcessor() {
        return new DynamicPluginProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginContextFactory pluginContextFactory() {
        return new DefaultPluginContextFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginTreeFactory pluginTreeFactory() {
        return new DefaultPluginTreeFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginHandlerChainFactory pluginHandlerChainFactory() {
        return new DefaultPluginHandlerChainFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginRepository pluginRepository() {
        return new DefaultPluginRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginEventPublisher pluginEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new ApplicationConnectionEventPublisher(eventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginLogger pluginLogger() {
        return new CommonsPluginLogger();
    }

    @Bean
    @ConditionalOnProperty(name = "concept.plugin.logger.standard.enabled", havingValue = "true", matchIfMissing = true)
    public PluginStandardLogger pluginStandardLogger() {
        return new PluginStandardLogger();
    }

    @Bean
    @ConditionalOnProperty(name = "concept.plugin.logger.error.enabled", havingValue = "true", matchIfMissing = true)
    public PluginErrorLogger pluginErrorLogger(PluginLogger logger) {
        return new PluginErrorLogger(logger);
    }

    @Bean
    public PluginFactory pluginJarPluginFactory(PluginConceptProperties properties) {
        String mode = properties.getJar().getMode().name();
        Class<? extends Plugin.StandardMetadata> standardType = properties.getMetadata().getStandardType();
        BinderMetadataJarPluginFactory factory = new BinderMetadataJarPluginFactory();
        factory.setDefaultMode(mode);
        if (standardType != null) {
            factory.setStandardMetadataType(standardType);
        }
        return factory;
    }

    @Bean
    public JarSubPluginFactory pluginJarSubPluginFactory() {
        return new JarSubPluginFactory();
    }

    @Bean
    public EntryResolver pluginEntryResolver() {
        return new EntryResolver();
    }

    @Bean
    public ContentResolver pluginContentResolver() {
        return new ContentResolver();
    }

    @Bean
    public PropertiesResolver pluginPropertiesResolver() {
        return new PropertiesResolver();
    }

    @Bean
    public ClassResolver pluginClassResolver() {
        return new ClassResolver();
    }

    @Bean
    public BeanResolver pluginBeanResolver() {
        return new BeanResolver();
    }

    @Bean
    @Order(100)
    public MethodPluginExtractor.InvokerFactory pluginObjectExtractorInvokerFactory() {
        return new PluginObjectExtractor.InvokerFactory();
    }

    @Bean
    @Order(200)
    public MethodPluginExtractor.InvokerFactory pluginContextExtractorInvokerFactory() {
        return new PluginContextExtractor.InvokerFactory();
    }

    @Bean
    @Order(300)
    public MethodPluginExtractor.InvokerFactory pluginPropertiesExtractorInvokerFactory() {
        return new PropertiesExtractor.InvokerFactory();
    }

    @Bean
    @Order(400)
    public MethodPluginExtractor.InvokerFactory pluginContentExtractorInvokerFactory() {
        return new ContentExtractor.InvokerFactory();
    }

    @Bean
    @Order(500)
    public MethodPluginExtractor.InvokerFactory pluginClassExtractorInvokerFactory() {
        return new ClassExtractor.InvokerFactory();
    }

    @Bean
    @Order(600)
    public MethodPluginExtractor.InvokerFactory pluginBeanExtractorInvokerFactory() {
        return new BeanExtractor.InvokerFactory();
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public PluginConcept pluginConcept(PluginContextFactory contextFactory,
                                       PluginHandlerChainFactory handlerChainFactory,
                                       PluginTreeFactory treeFactory,
                                       PluginRepository repository,
                                       PluginEventPublisher eventPublisher,
                                       PluginLogger logger,
                                       List<PluginFactory> factories,
                                       List<PluginHandler> handlers,
                                       List<PluginHandlerFactory> handlerFactories,
                                       List<PluginEventListener> eventListeners) {
        return new DefaultPluginConcept.Builder()
                .contextFactory(contextFactory)
                .handlerChainFactory(handlerChainFactory)
                .treeFactory(treeFactory)
                .repository(repository)
                .eventPublisher(eventPublisher)
                .logger(logger)
                .addFactories(factories)
                .addHandlers(handlers)
                .addHandlerFactories(handlerFactories)
                .addEventListeners(eventListeners)
                .build();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnPluginAutoloadEnabled
    public static class AutoloadConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PluginLocation.Filter pluginJarLocationFilter() {
            return new JarLocationFilter();
        }

        @Bean(destroyMethod = "shutdown")
        @ConditionalOnMissingBean
        public PluginExecutor pluginExecutor() {
            return new DefaultPluginExecutor();
        }

        @Bean
        @ConditionalOnMissingBean
        public PluginLocation pluginLocation(PluginConceptProperties properties,
                                             PluginLocation.Filter filter) {
            String basePath = properties.getAutoload().getLocation().getBasePath();
            return new LocalPluginLocation(basePath, filter);
        }

        @Bean(initMethod = "start", destroyMethod = "stop")
        @ConditionalOnMissingBean
        public PluginAutoLoader pluginAutoLoader(PluginConcept concept,
                                                 PluginExecutor executor,
                                                 PluginLocation location,
                                                 PluginConceptProperties properties) {
            return new DefaultPluginAutoLoader(concept, executor, location,
                    properties.getAutoload().getPeriod());
        }
    }
}
