package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.event.ApplicationConnectionEventPublisher;
import com.github.linyuzai.plugin.autoconfigure.processor.DynamicPluginProcessor;
import com.github.linyuzai.plugin.core.concept.DefaultPluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.DefaultPluginContextFactory;
import com.github.linyuzai.plugin.core.context.PluginContextFactory;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.handle.DefaultPluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChainFactory;
import com.github.linyuzai.plugin.core.resolve.ContentResolver;
import com.github.linyuzai.plugin.core.resolve.EntryResolver;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import com.github.linyuzai.plugin.core.resolve.PropertiesResolver;
import com.github.linyuzai.plugin.core.tree.DefaultPluginTreeFactory;
import com.github.linyuzai.plugin.core.tree.PluginTreeFactory;
import com.github.linyuzai.plugin.jar.factory.JarPluginFactory;
import com.github.linyuzai.plugin.jar.factory.JarSubPluginFactory;
import com.github.linyuzai.plugin.jar.resolve.JarClassNameResolver;
import com.github.linyuzai.plugin.jar.resolve.JarClassResolver;
import com.github.linyuzai.plugin.jar.resolve.JarInstanceResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
    public PluginHandlerChainFactory pluginHandlerChainFactory() {
        return new DefaultPluginHandlerChainFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginTreeFactory pluginTreeFactory() {
        return new DefaultPluginTreeFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginEventPublisher pluginEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new ApplicationConnectionEventPublisher(eventPublisher);
    }

    @Bean
    public JarPluginFactory jarPluginFactory() {
        return new JarPluginFactory();
    }

    @Bean
    public JarSubPluginFactory jarSubPluginFactory() {
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
    public JarClassNameResolver jarClassNameResolver() {
        return new JarClassNameResolver();
    }

    @Bean
    public JarInstanceResolver jarInstanceResolver() {
        return new JarInstanceResolver();
    }

    @Bean
    public JarClassResolver jarClassResolver() {
        return new JarClassResolver();
    }

    @Bean(initMethod = "initialize", destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public PluginConcept pluginConcept(PluginContextFactory contextFactory,
                                       PluginHandlerChainFactory handlerChainFactory,
                                       PluginTreeFactory treeFactory,
                                       PluginEventPublisher eventPublisher,
                                       List<PluginFactory> factories,
                                       List<PluginResolver> resolvers,
                                       List<PluginFilter> filters,
                                       List<PluginExtractor> extractors,
                                       List<PluginEventListener> eventListeners) {
        return new DefaultPluginConcept.Builder()
                .contextFactory(contextFactory)
                .handlerChainFactory(handlerChainFactory)
                .treeFactory(treeFactory)
                .eventPublisher(eventPublisher)
                .addFactories(factories)
                .addResolvers(resolvers)
                .addFilters(filters)
                .addExtractors(extractors)
                .addEventListeners(eventListeners)
                .build();
    }
}
