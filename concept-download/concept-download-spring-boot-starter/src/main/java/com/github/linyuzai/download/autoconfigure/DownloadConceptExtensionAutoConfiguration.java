package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.source.okhttp.OkHttpSourceFactory;
import com.github.linyuzai.download.core.source.reactive.PublisherSourceFactory;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 扩展配置。
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptCoreAutoConfiguration.class)
public class DownloadConceptExtensionAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(OkHttpClient.class)
    public static class OkHttpSourceAutoConfiguration {

        @Bean
        @Order(50)
        @ConditionalOnMissingBean
        public OkHttpSourceFactory okHttpSourceFactory() {
            return new OkHttpSourceFactory();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Mono.class, Flux.class})
    public static class ReactorSourceAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public PublisherSourceFactory publisherSourceFactory() {
            return new PublisherSourceFactory();
        }
    }
}
