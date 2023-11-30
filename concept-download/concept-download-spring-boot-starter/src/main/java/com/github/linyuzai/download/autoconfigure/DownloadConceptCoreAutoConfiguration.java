package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.event.ApplicationDownloadEventPublisher;
import com.github.linyuzai.download.autoconfigure.logger.CommonsDownloadLogger;
import com.github.linyuzai.download.autoconfigure.properties.DownloadProperties;
import com.github.linyuzai.download.autoconfigure.source.classpath.ClassPathPrefixSourceFactory;
import com.github.linyuzai.download.autoconfigure.source.classpath.ClassPathSourceFactory;
import com.github.linyuzai.download.autoconfigure.source.reactive.WebClientSourceFactory;
import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.cache.CacheNameGeneratorInitializer;
import com.github.linyuzai.download.core.cache.TimestampCacheNameGenerator;
import com.github.linyuzai.download.core.compress.DefaultSourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.SourceCompressor;
import com.github.linyuzai.download.core.compress.SourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.zip.ZipSourceCompressor;
import com.github.linyuzai.download.core.context.DefaultDownloadContextFactory;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.impl.*;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.logger.DownloadLogger;
import com.github.linyuzai.download.core.logger.ProgressCalculationLogger;
import com.github.linyuzai.download.core.logger.StandardDownloadLogger;
import com.github.linyuzai.download.core.logger.TimeSpentCalculationLogger;
import com.github.linyuzai.download.core.source.DefaultSourceFactoryAdapter;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import com.github.linyuzai.download.core.source.file.FilePrefixSourceFactory;
import com.github.linyuzai.download.core.source.file.FileSourceFactory;
import com.github.linyuzai.download.core.source.file.UserHomeSourceFactory;
import com.github.linyuzai.download.core.source.http.HttpSourceFactory;
import com.github.linyuzai.download.core.source.multiple.ArraySourceFactory;
import com.github.linyuzai.download.core.source.multiple.CollectionSourceFactory;
import com.github.linyuzai.download.core.source.reflect.ReflectionSourceFactory;
import com.github.linyuzai.download.core.source.original.OriginalSourceFactory;
import com.github.linyuzai.download.core.source.text.TextSourceFactory;
import com.github.linyuzai.download.core.write.BufferedDownloadWriter;
import com.github.linyuzai.download.core.write.DefaultDownloadWriterAdapter;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.DownloadWriterAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.util.List;

/**
 * 基础组件的配置。
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(DownloadProperties.class)
public class DownloadConceptCoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DownloadLogger downloadLogger() {
        return new CommonsDownloadLogger();
    }

    @Bean
    @ConditionalOnMissingBean
    public StandardDownloadLogger standardDownloadLogger(DownloadProperties properties) {
        boolean enabled = properties.getLogger().getStandard().isEnabled() &&
                properties.getLogger().isEnabled();
        StandardDownloadLogger logger = new StandardDownloadLogger();
        logger.setEnabled(enabled);
        return logger;
    }

    @Bean
    @ConditionalOnMissingBean
    public TimeSpentCalculationLogger timeSpentCalculationLogger(DownloadProperties properties) {
        boolean enabled = properties.getLogger().getTimeSpent().isEnabled() &&
                properties.getLogger().isEnabled();
        TimeSpentCalculationLogger logger = new TimeSpentCalculationLogger();
        logger.setEnabled(enabled);
        return logger;
    }

    @Bean
    @ConditionalOnMissingBean
    public ProgressCalculationLogger progressCalculationLogger(DownloadProperties properties) {
        boolean enabled = properties.getLogger().getProgress().isEnabled() &&
                properties.getLogger().isEnabled();
        Duration duration = Duration.ofMillis(properties.getLogger().getProgress().getDuration());
        boolean percentage = properties.getLogger().getProgress().isPercentage();
        ProgressCalculationLogger logger = new ProgressCalculationLogger(duration, percentage);
        logger.setEnabled(enabled);
        return logger;
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadEventPublisher downloadEventPublisher(List<DownloadEventListener> listeners) {
        return new ApplicationDownloadEventPublisher(listeners);
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
    @Order(0)
    @ConditionalOnMissingBean
    public CollectionSourceFactory collectionSourceFactory() {
        return new CollectionSourceFactory();
    }

    @Bean
    @Order(0)
    @ConditionalOnMissingBean
    public ArraySourceFactory arraySourceFactory() {
        return new ArraySourceFactory();
    }

    @Bean
    @Order(0)
    @ConditionalOnMissingBean
    public OriginalSourceFactory originalSourceFactory() {
        return new OriginalSourceFactory();
    }

    @Bean
    @Order(100)
    @ConditionalOnMissingBean
    public FileSourceFactory fileSourceFactory() {
        return new FileSourceFactory();
    }

    @Bean
    @Order(100)
    @ConditionalOnMissingBean
    public FilePrefixSourceFactory filePrefixSourceFactory() {
        return new FilePrefixSourceFactory();
    }

    @Bean
    @Order(100)
    @ConditionalOnMissingBean
    public UserHomeSourceFactory userHomeSourceFactory() {
        return new UserHomeSourceFactory();
    }

    @Bean
    @Order(100)
    @ConditionalOnMissingBean
    public ClassPathSourceFactory classPathResourceSourceFactory() {
        return new ClassPathSourceFactory();
    }

    @Bean
    @Order(100)
    @ConditionalOnMissingBean
    public ClassPathPrefixSourceFactory classPathPrefixSourceFactory() {
        return new ClassPathPrefixSourceFactory();
    }

    @Bean
    @Order(100)
    @ConditionalOnMissingBean
    public HttpSourceFactory httpSourceFactory() {
        return new HttpSourceFactory();
    }

    @Bean
    @Order(100)
    @ConditionalOnMissingBean
    public ReflectionSourceFactory reflectionSourceFactory() {
        return new ReflectionSourceFactory();
    }

    @Bean
    @Order(200)
    @ConditionalOnMissingBean
    public TextSourceFactory textSourceFactory() {
        return new TextSourceFactory();
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
    @Order(DownloadHandler.ORDER_CREATE_SOURCE)
    @ConditionalOnMissingBean
    public CreateSourceHandler createSourceHandler(SourceFactoryAdapter adapter) {
        return new CreateSourceHandler(adapter);
    }

    @Bean
    @Order(DownloadHandler.ORDER_LOAD_SOURCE)
    @ConditionalOnMissingBean
    public LoadSourceHandler loadSourceHandler(SourceLoader loader) {
        return new LoadSourceHandler(loader);
    }

    @Bean
    @Order(DownloadHandler.ORDER_COMPRESS_SOURCE)
    @ConditionalOnMissingBean
    public CompressSourceHandler compressSourceHandler(SourceCompressorAdapter adapter) {
        return new CompressSourceHandler(adapter);
    }

    @Bean
    @Order(DownloadHandler.ORDER_WRITE_RESPONSE)
    @ConditionalOnMissingBean
    public WriteResponseHandler writeResponseHandler(DownloadWriterAdapter adapter) {
        return new WriteResponseHandler(adapter);
    }

    @Bean
    @Order(DownloadHandler.ORDER_ASYNC_CONSUME)
    @ConditionalOnMissingBean
    public AsyncConsumeHandler asyncConsumeHandler() {
        return new AsyncConsumeHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadContextFactory downloadContextFactory() {
        return new DefaultDownloadContextFactory();
    }
}
