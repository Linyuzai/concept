package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.source.reactive.WebClientSourceFactory;
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
    @ConditionalOnMissingBean
    public WebClientSourceFactory webClientSourceFactory() {
        return new WebClientSourceFactory();
    }
}
