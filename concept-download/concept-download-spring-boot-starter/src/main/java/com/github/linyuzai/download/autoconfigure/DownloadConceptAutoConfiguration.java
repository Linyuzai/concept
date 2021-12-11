package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.properties.DownloadConceptProperties;
import com.github.linyuzai.download.core.compress.CompressSourceInterceptor;
import com.github.linyuzai.download.core.compress.DefaultSourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.SourceCompressor;
import com.github.linyuzai.download.core.compress.SourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.zip.ZipSourceCompressor;
import com.github.linyuzai.download.core.concept.ChainDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.configuration.DownloadConfigurer;
import com.github.linyuzai.download.core.context.*;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.loader.SourceLoader;
import com.github.linyuzai.download.core.loader.LoadSourceInterceptor;
import com.github.linyuzai.download.core.loader.SerialSourceLoader;
import com.github.linyuzai.download.core.source.DefaultSourceFactoryAdapter;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.core.response.WriteResponseInterceptor;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.CreateSourceInterceptor;
import com.github.linyuzai.download.core.source.file.FileSourceFactory;
import com.github.linyuzai.download.core.source.file.FilePrefixSourceFactory;
import com.github.linyuzai.download.core.source.file.UserHomeSourceFactory;
import com.github.linyuzai.download.core.source.multiple.ArraySourceFactory;
import com.github.linyuzai.download.core.source.multiple.CollectionSourceFactory;
import com.github.linyuzai.download.core.source.direct.DirectSourceFactory;
import com.github.linyuzai.download.core.writer.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(DownloadConceptProperties.class)
public class DownloadConceptAutoConfiguration {

    //private static final Log logger = LogFactory.getLog(DownloadConceptAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public DownloadContextFactory downloadContextFactory() {
        return new DefaultDownloadContextFactory();
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
    public DirectSourceFactory directSourceFactory() {
        return new DirectSourceFactory();
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
    public SourceFactoryAdapter sourceFactoryAdapter(List<SourceFactory> factories) {
        return new DefaultSourceFactoryAdapter(factories);
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
    public InitializeContextInterceptor initializeContextInterceptor(List<DownloadContextInitializer> initializers) {
        return new InitializeContextInterceptor(initializers);
    }

    @Bean
    @ConditionalOnMissingBean
    public CreateSourceInterceptor createSourceInterceptor(SourceFactoryAdapter adapter) {
        return new CreateSourceInterceptor(adapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceLoader sourceLoader() {
        return new SerialSourceLoader();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadSourceInterceptor loadSourceInterceptor(SourceLoader loader) {
        return new LoadSourceInterceptor(loader);
    }

    @Bean
    @ConditionalOnMissingBean
    public CompressSourceInterceptor compressSourceInterceptor(SourceCompressorAdapter adapter) {
        return new CompressSourceInterceptor(adapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public WriteResponseInterceptor writeResponseInterceptor(DownloadWriterAdapter adapter,
                                                             DownloadRequestProvider requestProvider,
                                                             DownloadResponseProvider responseProvider) {
        return new WriteResponseInterceptor(adapter, requestProvider, responseProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public DestroyContextInterceptor destroyContextInterceptor(List<DownloadContextDestroyer> destroyers) {
        return new DestroyContextInterceptor(destroyers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadConcept downloadConcept(DownloadConfiguration configuration, List<DownloadConfigurer> configurers,
                                           DownloadContextFactory factory, List<DownloadInterceptor> interceptors) {
        return new ChainDownloadConcept(configuration, configurers, factory, interceptors);
    }
}
