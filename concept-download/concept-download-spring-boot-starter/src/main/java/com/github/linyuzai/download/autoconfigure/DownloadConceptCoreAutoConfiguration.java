package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.cache.CacheNameGeneratorInitializer;
import com.github.linyuzai.download.core.cache.TimestampCacheNameGenerator;
import com.github.linyuzai.download.core.event.ApplicationDownloadEventPublisher;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.event.DownloadEventPublisherInitializer;
import com.github.linyuzai.download.core.handler.impl.CompressSourceHandler;
import com.github.linyuzai.download.core.compress.DefaultSourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.SourceCompressor;
import com.github.linyuzai.download.core.compress.SourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.zip.ZipSourceCompressor;
import com.github.linyuzai.download.core.concept.ChainDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadReturnInterceptor;
import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.configuration.DownloadConfigurer;
import com.github.linyuzai.download.core.context.*;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.impl.*;
import com.github.linyuzai.download.core.load.DefaultSourceLoader;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.source.DefaultSourceFactoryAdapter;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import com.github.linyuzai.download.core.source.classpath.ClassPathPrefixSourceFactory;
import com.github.linyuzai.download.core.source.classpath.ClassPathSourceFactory;
import com.github.linyuzai.download.core.source.file.FilePrefixSourceFactory;
import com.github.linyuzai.download.core.source.file.FileSourceFactory;
import com.github.linyuzai.download.core.source.file.UserHomeSourceFactory;
import com.github.linyuzai.download.core.source.http.HttpSourceFactory;
import com.github.linyuzai.download.core.source.multiple.ArraySourceFactory;
import com.github.linyuzai.download.core.source.multiple.CollectionSourceFactory;
import com.github.linyuzai.download.core.source.reflect.ReflectionSourceFactory;
import com.github.linyuzai.download.core.source.self.SelfSourceFactory;
import com.github.linyuzai.download.core.source.text.TextSourceFactory;
import com.github.linyuzai.download.core.web.DownloadRequestProvider;
import com.github.linyuzai.download.core.web.DownloadResponseProvider;
import com.github.linyuzai.download.core.write.BufferedDownloadWriter;
import com.github.linyuzai.download.core.write.DefaultDownloadWriterAdapter;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.DownloadWriterAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 基础组件的配置 / Configuration of basic components
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DownloadConfiguration.class)
public class DownloadConceptCoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DownloadContextFactory downloadContextFactory() {
        return new MapDownloadContextFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadEventPublisher downloadEventPublisher(List<DownloadEventListener> listeners) {
        return new ApplicationDownloadEventPublisher(listeners);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadEventPublisherInitializer downloadEventPublisherInitializer(DownloadEventPublisher publisher) {
        return new DownloadEventPublisherInitializer(publisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public BufferedDownloadWriter bufferedDownloadWriter() {
        return new BufferedDownloadWriter();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadWriterAdapter downloadWriterAdapter(List<DownloadWriter> writers) {
        return new DefaultDownloadWriterAdapter(writers);
    }

    @Bean
    @ConditionalOnMissingBean
    public CollectionSourceFactory collectionSourceFactory() {
        return new CollectionSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ArraySourceFactory arraySourceFactory() {
        return new ArraySourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public SelfSourceFactory selfSourceFactory() {
        return new SelfSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public FileSourceFactory fileSourceFactory() {
        return new FileSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilePrefixSourceFactory filePrefixSourceFactory() {
        return new FilePrefixSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserHomeSourceFactory userHomeSourceFactory() {
        return new UserHomeSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public TextSourceFactory textSourceFactory() {
        return new TextSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClassPathSourceFactory classPathResourceSourceFactory() {
        return new ClassPathSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClassPathPrefixSourceFactory classPathPrefixSourceFactory() {
        return new ClassPathPrefixSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpSourceFactory httpSourceFactory() {
        return new HttpSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReflectionSourceFactory reflectionSourceFactory() {
        return new ReflectionSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceFactoryAdapter sourceFactoryAdapter(List<SourceFactory> factories) {
        return new DefaultSourceFactoryAdapter(factories);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheNameGenerator cacheNameGenerator() {
        return new TimestampCacheNameGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheNameGeneratorInitializer cacheNameGeneratorInitializer(CacheNameGenerator generator) {
        return new CacheNameGeneratorInitializer(generator);
    }

    @Bean
    @ConditionalOnMissingBean
    public ZipSourceCompressor zipSourceCompressor() {
        return new ZipSourceCompressor();
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceCompressorAdapter sourceCompressorAdapter(List<SourceCompressor> compressors) {
        return new DefaultSourceCompressorAdapter(compressors);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitializeContextHandler initializeContextHandler(List<DownloadContextInitializer> initializers) {
        return new InitializeContextHandler(initializers);
    }

    @Bean
    @ConditionalOnMissingBean
    public CreateSourceHandler createSourceHandler(SourceFactoryAdapter adapter) {
        return new CreateSourceHandler(adapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceLoader sourceLoader() {
        return new DefaultSourceLoader();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadSourceHandler loadSourceHandler(SourceLoader loader) {
        return new LoadSourceHandler(loader);
    }

    @Bean
    @ConditionalOnMissingBean
    public CompressSourceHandler compressSourceHandler(SourceCompressorAdapter adapter) {
        return new CompressSourceHandler(adapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public WriteResponseHandler writeResponseHandler(DownloadWriterAdapter adapter,
                                                     DownloadRequestProvider requestProvider,
                                                     DownloadResponseProvider responseProvider) {
        return new WriteResponseHandler(adapter, requestProvider, responseProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public DestroyContextHandler destroyContextHandler(List<DownloadContextDestroyer> destroyers) {
        return new DestroyContextHandler(destroyers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadConcept downloadConcept(DownloadConfiguration configuration,
                                           DownloadContextFactory factory,
                                           DownloadReturnInterceptor returnInterceptor,
                                           List<DownloadHandler> handlers,
                                           List<DownloadConfigurer> configurers) {
        configurers.forEach(it -> it.configure(configuration));
        return new ChainDownloadConcept(configuration, factory, returnInterceptor, handlers);
    }
}
