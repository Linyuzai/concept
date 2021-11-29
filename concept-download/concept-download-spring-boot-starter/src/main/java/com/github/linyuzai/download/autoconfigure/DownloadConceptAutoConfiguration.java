package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.info.DownloadConceptInfo;
import com.github.linyuzai.download.core.compress.CompressSourceInterceptor;
import com.github.linyuzai.download.core.compress.SourceCompressor;
import com.github.linyuzai.download.core.compress.zip.ZipSourceCompressor;
import com.github.linyuzai.download.core.concept.ChainDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DefaultDownloadContextFactory;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.loader.DownloadSourceLoader;
import com.github.linyuzai.download.core.loader.LoadSourceInterceptor;
import com.github.linyuzai.download.core.loader.SerialDownloadSourceLoader;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.request.ProvideRequestInterceptor;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.core.response.ProvideResponseInterceptor;
import com.github.linyuzai.download.core.response.WriteResponseInterceptor;
import com.github.linyuzai.download.core.source.DownloadSourceFactory;
import com.github.linyuzai.download.core.source.PrepareSourceInterceptor;
import com.github.linyuzai.download.core.source.file.FileDownloadSourceFactory;
import com.github.linyuzai.download.core.source.file.FilePathDownloadSourceFactory;
import com.github.linyuzai.download.core.source.file.UserHomeDownloadSourceFactory;
import com.github.linyuzai.download.core.source.multiple.ArrayDownloadSourceFactory;
import com.github.linyuzai.download.core.source.multiple.CollectionDownloadSourceFactory;
import com.github.linyuzai.download.core.source.original.OriginalDownloadSourceFactory;
import com.github.linyuzai.download.core.writer.BufferedSourceWriter;
import com.github.linyuzai.download.core.writer.SourceWriter;
import com.github.linyuzai.download.core.writer.SourceWriterInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    public ProvideRequestInterceptor provideRequestInterceptor(DownloadRequestProvider provider) {
        return new ProvideRequestInterceptor(provider);
    }

    @Bean
    @ConditionalOnMissingBean
    public ProvideResponseInterceptor provideResponseInterceptor(DownloadResponseProvider provider) {
        return new ProvideResponseInterceptor(provider);
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceWriter sourceWriter() {
        return new BufferedSourceWriter();
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceWriterInterceptor sourceWriterInterceptor(SourceWriter writer) {
        return new SourceWriterInterceptor(writer);
    }

    @Bean
    @ConditionalOnMissingBean
    public CollectionDownloadSourceFactory collectionDownloadSourceFactory() {
        return new CollectionDownloadSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ArrayDownloadSourceFactory arrayDownloadSourceFactory() {
        return new ArrayDownloadSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public OriginalDownloadSourceFactory originalDownloadSourceFactory() {
        return new OriginalDownloadSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public FileDownloadSourceFactory fileDownloadSourceFactory() {
        return new FileDownloadSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilePathDownloadSourceFactory filePathDownloadSourceFactory() {
        return new FilePathDownloadSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserHomeDownloadSourceFactory userHomeDownloadSourceFactory() {
        return new UserHomeDownloadSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public PrepareSourceInterceptor prepareSourceInterceptor(List<DownloadSourceFactory> factories) {
        return new PrepareSourceInterceptor(factories);
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadSourceLoader downloadSourceLoader() {
        return new SerialDownloadSourceLoader();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadSourceInterceptor loadSourceInterceptor(DownloadSourceLoader loader) {
        return new LoadSourceInterceptor(loader);
    }

    @Bean
    @ConditionalOnMissingBean
    public ZipSourceCompressor zipSourceCompressor() {
        //MultipartConfigElement
        return new ZipSourceCompressor("/Users/tanghanzheng/IdeaProjects/Github/cache");
    }

    @Bean
    @ConditionalOnMissingBean
    public CompressSourceInterceptor compressSourceInterceptor(List<SourceCompressor> compressors) {
        return new CompressSourceInterceptor(compressors);
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
