package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.concept.DownloadReturnInterceptor;
import com.github.linyuzai.download.core.source.reactive.WebClientSourceFactory;
import com.github.linyuzai.download.core.web.DownloadRequestProvider;
import com.github.linyuzai.download.core.web.DownloadResponseProvider;
import com.github.linyuzai.download.core.web.reactive.ReactiveDownloadFilter;
import com.github.linyuzai.download.core.web.reactive.ReactiveDownloadRequestProvider;
import com.github.linyuzai.download.core.web.reactive.ReactiveDownloadResponseProvider;
import com.github.linyuzai.download.core.web.reactive.ReactiveDownloadReturnInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reactive 环境配置。
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptCoreAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class DownloadConceptReactiveAutoConfiguration {

    @Bean
    public ReactiveDownloadFilter reactiveDownloadFilter() {
        return new ReactiveDownloadFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebClientSourceFactory webClientSourceFactory() {
        return new WebClientSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadRequestProvider downloadRequestProvider() {
        return new ReactiveDownloadRequestProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadResponseProvider downloadResponseProvider() {
        return new ReactiveDownloadResponseProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadReturnInterceptor downloadReturnInterceptor() {
        return new ReactiveDownloadReturnInterceptor();
    }
}
