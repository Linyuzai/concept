package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.compress.CompressOriginalSourceInterceptor;
import com.github.linyuzai.download.core.compress.OriginalSourceCompressor;
import com.github.linyuzai.download.core.compress.zip.ZipOriginalSourceCompressor;
import com.github.linyuzai.download.core.concept.ChainDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DefaultDownloadContextFactory;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.context.InitializeContextInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.loader.OriginalSourceLoader;
import com.github.linyuzai.download.core.loader.LoadOriginalSourceInterceptor;
import com.github.linyuzai.download.core.loader.SerialOriginalSourceLoader;
import com.github.linyuzai.download.core.original.DefaultOriginalSourceFactoryAdapter;
import com.github.linyuzai.download.core.original.OriginalSourceFactoryAdapter;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AutoConfigureAfter(DownloadConceptServletAutoConfiguration.class)
public class DownloadConceptAutoConfiguration {

    //private static final Log logger = LogFactory.getLog(DownloadConceptAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public DownloadContextFactory downloadContextFactory() {
        return new DefaultDownloadContextFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public BufferedOriginalSourceWriter bufferedOriginalSourceWriter() {
        return new BufferedOriginalSourceWriter();
    }

    @Bean
    @ConditionalOnMissingBean
    public BufferedCompressedSourceWriter bufferedCompressedSourceWriter() {
        return new BufferedCompressedSourceWriter();
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
    public InitializeContextInterceptor initializeContextInterceptor(DownloadRequestProvider downloadRequestProvider,
                                                                     DownloadResponseProvider downloadResponseProvider,
                                                                     SourceWriterAdapter sourceWriterAdapter,
                                                                     OriginalSourceFactoryAdapter originalSourceFactoryAdapter) {
        return new InitializeContextInterceptor(
                downloadRequestProvider, downloadResponseProvider,
                sourceWriterAdapter, originalSourceFactoryAdapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public CreateOriginalSourceInterceptor createOriginalSourceInterceptor() {
        return new CreateOriginalSourceInterceptor();
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
    public ZipOriginalSourceCompressor zipOriginalSourceCompressor() {
        //MultipartConfigElement
        return new ZipOriginalSourceCompressor("/Users/tanghanzheng/IdeaProjects/Github/cache");
    }

    @Bean
    @ConditionalOnMissingBean
    public CompressOriginalSourceInterceptor compressOriginalSourceInterceptor(List<OriginalSourceCompressor> compressors) {
        return new CompressOriginalSourceInterceptor(compressors);
    }

    @Bean
    @ConditionalOnMissingBean
    public WriteResponseInterceptor writeResponseInterceptor() {
        return new WriteResponseInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadConcept downloadConcept(DownloadContextFactory factory, List<DownloadInterceptor> interceptors) {
        return new ChainDownloadConcept(factory, interceptors);
    }
}
