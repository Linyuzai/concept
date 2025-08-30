package com.github.linyuzai.plugin.autoconfigure;

import com.amazonaws.services.s3.AmazonS3;
import com.github.linyuzai.plugin.autoconfigure.autoload.ConditionalOnPluginAutoloadEnabled;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanResolver;
import com.github.linyuzai.plugin.autoconfigure.event.ApplicationPluginEventPublisher;
import com.github.linyuzai.plugin.autoconfigure.logger.CommonsPluginLogger;
import com.github.linyuzai.plugin.autoconfigure.metadata.BinderPluginMetadataFactory;
import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.autoconfigure.processor.ConceptPluginProcessor;
import com.github.linyuzai.plugin.autoconfigure.yaml.YamlResolver;
import com.github.linyuzai.plugin.autoconfigure.yaml.properties.YamlPropertiesMetadataAdapter;
import com.github.linyuzai.plugin.autoconfigure.yaml.properties.YamlPropertiesResolver;
import com.github.linyuzai.plugin.aws.AmazonS3Storage;
import com.github.linyuzai.plugin.aws.S3ClientStorage;
import com.github.linyuzai.plugin.core.autoload.DefaultPluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.metadata.AbstractPluginMetadataFactory;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadataAdapter;
import com.github.linyuzai.plugin.core.storage.LocalPluginStorage;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
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
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import com.github.linyuzai.plugin.core.metadata.SubPluginMetadataFactory;
import com.github.linyuzai.plugin.core.repository.DefaultPluginRepository;
import com.github.linyuzai.plugin.core.repository.PluginRepository;
import com.github.linyuzai.plugin.core.storage.RemotePluginStorage;
import com.github.linyuzai.plugin.core.tree.DefaultPluginTreeFactory;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import com.github.linyuzai.plugin.jar.autoload.JarStorageFilter;
import com.github.linyuzai.plugin.jar.factory.ExJarPluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarFilePluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarStreamPluginFactory;
import com.github.linyuzai.plugin.jar.handle.extract.ClassExtractor;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassResolver;
import com.github.linyuzai.plugin.jar.metadata.JarFilePluginMetadataFactory;
import com.github.linyuzai.plugin.jar.metadata.JarStreamPluginMetadataFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.S3Client;

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
        return new ApplicationPluginEventPublisher(eventPublisher);
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
    public PropertiesMetadataAdapter propertiesMetadataAdapter() {
        return new PropertiesMetadataAdapter();
    }

    @Bean
    public YamlPropertiesMetadataAdapter yamlPropertiesMetadataAdapter() {
        return new YamlPropertiesMetadataAdapter();
    }

    @Bean
    public BinderPluginMetadataFactory jarFilePluginMetadataFactory(List<AbstractPluginMetadataFactory.Adapter> adapters,
                                                                    PluginConceptProperties properties) {
        JarFilePluginMetadataFactory factory = new JarFilePluginMetadataFactory();
        factory.setAdapters(adapters);
        return newBinderPluginMetadataFactory(factory, properties);
    }

    @Bean
    public BinderPluginMetadataFactory jarStreamPluginMetadataFactory(List<AbstractPluginMetadataFactory.Adapter> adapters,
                                                                      PluginConceptProperties properties) {
        JarStreamPluginMetadataFactory factory = new JarStreamPluginMetadataFactory();
        factory.setAdapters(adapters);
        return newBinderPluginMetadataFactory(factory, properties);
    }

    private BinderPluginMetadataFactory newBinderPluginMetadataFactory(PluginMetadataFactory metadataFactory,
                                                                       PluginConceptProperties properties) {
        BinderPluginMetadataFactory factory = new BinderPluginMetadataFactory();
        factory.setMetadataFactory(metadataFactory);
        Class<? extends Plugin.StandardMetadata> standardType = properties.getMetadata().getStandardType();
        if (standardType != null) {
            factory.setStandardMetadataType(standardType);
        }
        return factory;
    }

    @Bean
    public SubPluginMetadataFactory subPluginMetadataFactory() {
        return new SubPluginMetadataFactory();
    }

    @Bean
    public JarFilePluginFactory jarFilePluginFactory(PluginConceptProperties properties) {
        String mode = properties.getJar().getMode().name();
        JarFilePluginFactory factory = new JarFilePluginFactory();
        factory.setDefaultMode(mode);
        return factory;
    }

    @Bean
    public ExJarPluginFactory exJarPluginFactory(PluginConceptProperties properties) {
        String mode = properties.getJar().getMode().name();
        ExJarPluginFactory factory = new ExJarPluginFactory();
        factory.setDefaultMode(mode);
        return factory;
    }

    @Bean
    public JarStreamPluginFactory jarStreamPluginFactory() {
        return new JarStreamPluginFactory();
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
    public YamlResolver pluginYamlResolver() {
        return new YamlResolver();
    }

    @Bean
    public YamlPropertiesResolver pluginYamlPropertiesResolver() {
        return new YamlPropertiesResolver();
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
                                       List<PluginMetadataFactory> metadataFactories,
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
                .addMetadataFactories(metadataFactories)
                .addFactories(factories)
                .addHandlers(handlers)
                .addHandlerFactories(handlerFactories)
                .addEventListeners(eventListeners)
                .build();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "concept.plugin.storage.type", havingValue = "LOCAL", matchIfMissing = true)
    public static class LocalStorageConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PluginStorage pluginStorage(PluginConceptProperties properties) {
            String location = properties.getStorage().getLocation();
            String localLocation = StringUtils.hasText(location) ?
                    location : LocalPluginStorage.DEFAULT_LOCATION;
            return new LocalPluginStorage(localLocation, new JarStorageFilter());
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "concept.plugin.storage.type", havingValue = "AWS_V1")
    public static class AmazonS3StorageConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PluginStorage pluginStorage(PluginConceptProperties properties,
                                           AmazonS3 amazonS3) {
            String location = properties.getStorage().getLocation();
            String bucket = StringUtils.hasText(location) ?
                    location : RemotePluginStorage.DEFAULT_LOCATION;
            return new AmazonS3Storage(bucket, amazonS3);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "concept.plugin.storage.type", havingValue = "AWS_V2")
    public static class S3ClientStorageConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PluginStorage pluginStorage(PluginConceptProperties properties,
                                           S3Client s3Client) {
            String location = properties.getStorage().getLocation();
            String bucket = StringUtils.hasText(location) ?
                    location : RemotePluginStorage.DEFAULT_LOCATION;
            return new S3ClientStorage(bucket, s3Client);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnPluginAutoloadEnabled
    public static class AutoloadConfiguration {

        @Bean(destroyMethod = "shutdown")
        @ConditionalOnMissingBean
        public PluginExecutor pluginExecutor() {
            return new DefaultPluginExecutor();
        }

        @Bean(initMethod = "start", destroyMethod = "stop")
        @ConditionalOnMissingBean
        public PluginAutoLoader pluginAutoLoader(PluginConcept concept,
                                                 PluginExecutor executor,
                                                 PluginStorage storage,
                                                 PluginConceptProperties properties) {
            return new DefaultPluginAutoLoader(concept, executor, storage,
                    properties.getAutoload().getPeriod());
        }
    }
}
