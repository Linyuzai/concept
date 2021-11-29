package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.compress.CompressSourceInterceptor;
import com.github.linyuzai.download.core.compress.SourceCompressor;
import com.github.linyuzai.download.core.compress.zip.ZipSourceCompressor;
import com.github.linyuzai.download.core.concept.ChainDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DefaultDownloadContextFactory;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.loader.OriginalSourceLoader;
import com.github.linyuzai.download.core.loader.LoadSourceInterceptor;
import com.github.linyuzai.download.core.loader.SerialOriginalSourceLoader;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.request.ProvideRequestInterceptor;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.core.response.ProvideResponseInterceptor;
import com.github.linyuzai.download.core.response.WriteResponseInterceptor;
import com.github.linyuzai.download.core.original.OriginalSourceFactory;
import com.github.linyuzai.download.core.original.PrepareSourceInterceptor;
import com.github.linyuzai.download.core.original.file.FileOriginalSourceFactory;
import com.github.linyuzai.download.core.original.file.FilePathOriginalSourceFactory;
import com.github.linyuzai.download.core.original.file.UserHomeOriginalSourceFactory;
import com.github.linyuzai.download.core.original.multiple.ArrayOriginalSourceFactory;
import com.github.linyuzai.download.core.original.multiple.CollectionOriginalSourceFactory;
import com.github.linyuzai.download.core.original.direct.DirectOriginalSourceFactory;
import com.github.linyuzai.download.core.writer.BufferedSourceWriter;
import com.github.linyuzai.download.core.writer.SourceWriter;
import com.github.linyuzai.download.core.writer.SourceWriterInterceptor;
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
    public PrepareSourceInterceptor prepareSourceInterceptor(List<OriginalSourceFactory> factories) {
        return new PrepareSourceInterceptor(factories);
    }

    @Bean
    @ConditionalOnMissingBean
    public OriginalSourceLoader originalSourceLoader() {
        return new SerialOriginalSourceLoader();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadSourceInterceptor loadSourceInterceptor(OriginalSourceLoader loader) {
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
