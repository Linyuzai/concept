package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.web.reactive.request.ReactiveDownloadRequestProvider;
import com.github.linyuzai.download.web.reactive.response.ReactiveDownloadResponseProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reactive的配置 / Configuration of reactive
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass({ReactiveDownloadRequestProvider.class, ReactiveDownloadResponseProvider.class})
public class DownloadConceptWebReactiveAutoConfiguration {

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
}
