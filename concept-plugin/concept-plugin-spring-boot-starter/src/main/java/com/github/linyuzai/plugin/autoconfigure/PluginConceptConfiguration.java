package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanResolver;
import com.github.linyuzai.plugin.autoconfigure.event.ApplicationPluginEventPublisher;
import com.github.linyuzai.plugin.autoconfigure.logger.CommonsPluginLogger;
import com.github.linyuzai.plugin.autoconfigure.metadata.BinderPluginFactory;
import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.autoconfigure.processor.ConceptPluginProcessor;
import com.github.linyuzai.plugin.autoconfigure.yaml.YamlMetadataAdapter;
import com.github.linyuzai.plugin.autoconfigure.yaml.YamlResolver;
import com.github.linyuzai.plugin.core.concept.DefaultPluginConcept;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.DefaultPluginContextFactory;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.handle.DefaultPluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandlerFactory;
import com.github.linyuzai.plugin.core.handle.extract.*;
import com.github.linyuzai.plugin.core.handle.resolve.ContentResolver;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;
import com.github.linyuzai.plugin.core.intercept.AddListenersPluginInterceptor;
import com.github.linyuzai.plugin.core.intercept.NestedDepthPluginInterceptor;
import com.github.linyuzai.plugin.core.intercept.PluginInterceptor;
import com.github.linyuzai.plugin.core.listener.PluginListener;
import com.github.linyuzai.plugin.core.logger.PluginLogger;
import com.github.linyuzai.plugin.core.logger.PluginStandardLogger;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadataAdapter;
import com.github.linyuzai.plugin.core.path.DefaultPluginPathFactory;
import com.github.linyuzai.plugin.core.path.PluginPathFactory;
import com.github.linyuzai.plugin.core.repository.DefaultPluginRepository;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.tree.DefaultPluginTreeFactory;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import com.github.linyuzai.plugin.core.util.ReadUtils;
import com.github.linyuzai.plugin.jar.factory.JarStreamPluginFactory;
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
    public static ConceptPluginProcessor conceptPluginProcessor() {
        return new ConceptPluginProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginPathFactory pluginPathFactory() {
        return new DefaultPluginPathFactory();
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
    public NestedDepthPluginInterceptor nestedDepthPluginInterceptor(PluginConceptProperties properties) {
        long max = properties.getValidation().getMaxReadSize().toBytes();
        ReadUtils.setReadLimit(max);
        return new NestedDepthPluginInterceptor(properties.getValidation().getMaxNestedDepth());
    }

    @Bean
    public AddListenersPluginInterceptor addListenersPluginInterceptor(List<PluginListener> listeners) {
        return new AddListenersPluginInterceptor(listeners);
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
        return new ApplicationPluginEventPublisher(eventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginLogger pluginLogger() {
        return new CommonsPluginLogger();
    }

    @Bean
    @ConditionalOnProperty(name = "concept.plugin.logger.standard.enabled", havingValue = "true", matchIfMissing = true)
    public PluginStandardLogger pluginStandardLogger(PluginLogger logger) {
        return new PluginStandardLogger(logger);
    }

    @Bean
    public PropertiesMetadataAdapter propertiesMetadataAdapter() {
        return new PropertiesMetadataAdapter();
    }

    @Bean
    public YamlMetadataAdapter yamlPropertiesMetadataAdapter() {
        return new YamlMetadataAdapter();
    }

    @Bean
    public BinderPluginFactory jarStreamPluginFactory(PluginConceptProperties properties,
                                                      List<PluginMetadata.Adapter> adapters) {
        JarStreamPluginFactory pluginFactory = new JarStreamPluginFactory();
        pluginFactory.getMetadataAdapters().addAll(adapters);
        BinderPluginFactory factory = new BinderPluginFactory();
        factory.setPluginFactory(pluginFactory);
        factory.setMetadataFactory(pluginFactory);
        Class<? extends Plugin.StandardMetadata> standardType = properties.getMetadata().getStandardType();
        if (standardType != null) {
            factory.setStandardMetadataType(standardType);
        }
        return factory;
    }

    @Bean
    public EntryResolver pluginEntryResolver() {
        return new EntryResolver();
    }

    @Bean
    public ContentResolver pluginContentResolver() {
        return new ContentResolver();
    }

    /*@Bean
    public PropertiesResolver pluginPropertiesResolver() {
        return new PropertiesResolver();
    }*/

    @Bean
    public YamlResolver pluginYamlResolver() {
        return new YamlResolver();
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
    public PluginConcept pluginConcept(PluginPathFactory pathFactory,
                                       PluginContextFactory contextFactory,
                                       PluginHandlerChainFactory handlerChainFactory,
                                       PluginTreeFactory treeFactory,
                                       PluginStorage storage,
                                       PluginRepository repository,
                                       PluginEventPublisher eventPublisher,
                                       PluginLogger logger,
                                       List<PluginMetadataFactory> metadataFactories,
                                       List<PluginFactory> factories,
                                       List<PluginHandler> handlers,
                                       List<PluginHandlerFactory> handlerFactories,
                                       List<PluginInterceptor> interceptors,
                                       List<PluginEventListener> eventListeners) {
        return new DefaultPluginConcept.Builder()
                .pathFactory(pathFactory)
                .contextFactory(contextFactory)
                .handlerChainFactory(handlerChainFactory)
                .treeFactory(treeFactory)
                .storage(storage)
                .repository(repository)
                .eventPublisher(eventPublisher)
                .logger(logger)
                .addMetadataFactories(metadataFactories)
                .addFactories(factories)
                .addHandlers(handlers)
                .addHandlerFactories(handlerFactories)
                .addInterceptors(interceptors)
                .addEventListeners(eventListeners)
                .build();
    }
}
