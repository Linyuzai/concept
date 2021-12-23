package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.properties.DownloadConceptProperties;
import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.cache.CacheNameGeneratorInitializer;
import com.github.linyuzai.download.core.cache.TimestampCacheNameGenerator;
import com.github.linyuzai.download.core.compress.CompressSourceHandler;
import com.github.linyuzai.download.core.compress.DefaultSourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.SourceCompressor;
import com.github.linyuzai.download.core.compress.SourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.zip.ZipSourceCompressor;
import com.github.linyuzai.download.core.concept.ChainDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.configuration.DownloadConfigurer;
import com.github.linyuzai.download.core.context.*;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.load.*;
import com.github.linyuzai.download.core.source.DefaultSourceFactoryAdapter;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.core.response.WriteResponseHandler;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.CreateSourceHandler;
import com.github.linyuzai.download.core.source.file.FileSourceFactory;
import com.github.linyuzai.download.core.source.file.FilePrefixSourceFactory;
import com.github.linyuzai.download.core.source.file.UserHomeSourceFactory;
import com.github.linyuzai.download.core.source.inputstream.InputStreamSourceFactory;
import com.github.linyuzai.download.core.source.multiple.ArraySourceFactory;
import com.github.linyuzai.download.core.source.multiple.CollectionSourceFactory;
import com.github.linyuzai.download.core.source.reflection.ReflectionSourceFactory;
import com.github.linyuzai.download.core.source.self.SelfSourceFactory;
import com.github.linyuzai.download.core.source.text.TextSourceFactory;
import com.github.linyuzai.download.core.writer.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 基础组件的配置 / Configuration of basic components
 */
@Configuration
@EnableConfigurationProperties(DownloadConceptProperties.class)
public class DownloadConceptAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DownloadConfiguration downloadConfiguration(DownloadConceptProperties properties) {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.getResponse().setContentType(properties.getResponse().getContentType());
        configuration.getResponse().setHeaders(properties.getResponse().getHeaders());
        configuration.getSource().getCache().setEnabled(properties.getSource().getCache().isEnabled());
        configuration.getSource().getCache().setPath(properties.getSource().getCache().getPath());
        configuration.getSource().getCache().setDelete(properties.getSource().getCache().isDelete());
        configuration.getCompress().setFormat(properties.getCompress().getFormat());
        configuration.getCompress().getCache().setEnabled(properties.getCompress().getCache().isEnabled());
        configuration.getCompress().getCache().setPath(properties.getCompress().getCache().getPath());
        configuration.getCompress().getCache().setDelete(properties.getCompress().getCache().isDelete());
        return configuration;
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadContextFactory downloadContextFactory() {
        return new MapDownloadContextFactory();
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
    public SelfSourceFactory directSourceFactory() {
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
    public InputStreamSourceFactory inputStreamSourceFactory() {
        return new InputStreamSourceFactory();
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
    public SourceLoaderInvoker sourceLoaderInvoker() {
        return new SerialSourceLoaderInvoker();
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceLoadExceptionHandler sourceLoadExceptionHandler() {
        return new RethrowLoadedSourceLoadExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceLoaderFactory sourceLoaderFactory(SourceLoadExceptionHandler handler) {
        return new ExceptionCaughtSourceLoaderFactory(handler);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadSourceHandler loadSourceHandler(SourceLoaderFactory factory,
                                               SourceLoaderInvoker invoker,
                                               SourceLoadExceptionHandler handler) {
        return new LoadSourceHandler(factory, invoker, handler);
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
    public DownloadConcept downloadConcept(DownloadConfiguration configuration, List<DownloadConfigurer> configurers,
                                           DownloadContextFactory factory, List<DownloadHandler> handlers) {
        configurers.forEach(it -> it.configure(configuration));
        return new ChainDownloadConcept(configuration, factory, handlers);
    }
}
