package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.cache.DownloadCacheLocation;
import com.github.linyuzai.download.core.cache.UserHomeDownloadCacheLocation;
import com.github.linyuzai.download.core.compress.CompressOriginalSourceInterceptor;
import com.github.linyuzai.download.core.compress.DefaultOriginalSourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.OriginalSourceCompressor;
import com.github.linyuzai.download.core.compress.OriginalSourceCompressorAdapter;
import com.github.linyuzai.download.core.compress.zip.ZipOriginalSourceCompressor;
import com.github.linyuzai.download.core.concept.ChainDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.*;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.loader.OriginalSourceLoader;
import com.github.linyuzai.download.core.loader.LoadOriginalSourceInterceptor;
import com.github.linyuzai.download.core.loader.SerialOriginalSourceLoader;
import com.github.linyuzai.download.core.original.DefaultOriginalSourceFactoryAdapter;
import com.github.linyuzai.download.core.original.OriginalSourceFactoryAdapter;
import com.github.linyuzai.download.core.response.WriteResponseInterceptor;
import com.github.linyuzai.download.core.original.OriginalSourceFactory;
import com.github.linyuzai.download.core.original.CreateOriginalSourceInterceptor;
import com.github.linyuzai.download.core.original.file.FileOriginalSourceFactory;
import com.github.linyuzai.download.core.original.file.FilePathOriginalSourceFactory;
import com.github.linyuzai.download.core.original.file.UserHomeOriginalSourceFactory;
import com.github.linyuzai.download.core.original.multiple.ArrayOriginalSourceFactory;
import com.github.linyuzai.download.core.original.multiple.CollectionOriginalSourceFactory;
import com.github.linyuzai.download.core.original.direct.DirectOriginalSourceFactory;
import com.github.linyuzai.download.core.writer.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DownloadConceptAutoConfiguration {

    //private static final Log logger = LogFactory.getLog(DownloadConceptAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public DownloadCacheLocation downloadCacheLocation() {
        return new UserHomeDownloadCacheLocation();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadContextFactory downloadContextFactory() {
        return new DefaultDownloadContextFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public BufferedSourceWriter bufferedSourceWriter() {
        return new BufferedSourceWriter();
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceWriterAdapter sourceWriterAdapter(List<SourceWriter> writers) {
        return new DefaultSourceWriterAdapter(writers);
    }

    @Bean
    @ConditionalOnMissingBean
    public CollectionOriginalSourceFactory collectionOriginalSourceFactory() {
        return new CollectionOriginalSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ArrayOriginalSourceFactory arrayOriginalSourceFactory() {
        return new ArrayOriginalSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public DirectOriginalSourceFactory directOriginalSourceFactory() {
        return new DirectOriginalSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public FileOriginalSourceFactory fileOriginalSourceFactory() {
        return new FileOriginalSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilePathOriginalSourceFactory filePathOriginalSourceFactory() {
        return new FilePathOriginalSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserHomeOriginalSourceFactory userHomeOriginalSourceFactory() {
        return new UserHomeOriginalSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public OriginalSourceFactoryAdapter originalSourceFactoryAdapter(List<OriginalSourceFactory> factories) {
        return new DefaultOriginalSourceFactoryAdapter(factories);
    }

    @Bean
    @ConditionalOnMissingBean
    public ZipOriginalSourceCompressor zipOriginalSourceCompressor() {
        return new ZipOriginalSourceCompressor();
    }

    @Bean
    @ConditionalOnMissingBean
    public OriginalSourceCompressorAdapter originalSourceCompressorAdapter(List<OriginalSourceCompressor> compressors) {
        return new DefaultOriginalSourceCompressorAdapter(compressors);
    }

    @Bean
    @ConditionalOnMissingBean
    public InitializeContextInterceptor initializeContextInterceptor(List<DownloadContextInitializer> initializers) {
        return new InitializeContextInterceptor(initializers);
    }

    @Bean
    @ConditionalOnMissingBean
    public CreateOriginalSourceInterceptor createOriginalSourceInterceptor(OriginalSourceFactoryAdapter adapter) {
        return new CreateOriginalSourceInterceptor(adapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public OriginalSourceLoader originalSourceLoader() {
        return new SerialOriginalSourceLoader();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadOriginalSourceInterceptor loadOriginalSourceInterceptor(OriginalSourceLoader loader) {
        return new LoadOriginalSourceInterceptor(loader);
    }

    @Bean
    @ConditionalOnMissingBean
    public CompressOriginalSourceInterceptor compressOriginalSourceInterceptor(OriginalSourceCompressorAdapter adapter,
                                                                               DownloadCacheLocation cacheLocation) {
        return new CompressOriginalSourceInterceptor(adapter, cacheLocation);
    }

    @Bean
    @ConditionalOnMissingBean
    public WriteResponseInterceptor writeResponseInterceptor(SourceWriterAdapter adapter) {
        return new WriteResponseInterceptor(adapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public DestroyContextInterceptor destroyContextInterceptor(List<DownloadContextDestroyer> destroyers) {
        return new DestroyContextInterceptor(destroyers);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadConcept downloadConcept(DownloadContextFactory factory, List<DownloadInterceptor> interceptors) {
        return new ChainDownloadConcept(factory, interceptors);
    }
}
