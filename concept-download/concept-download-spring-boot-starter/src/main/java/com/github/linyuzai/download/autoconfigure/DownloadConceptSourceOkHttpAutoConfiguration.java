package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.okhttp.OkHttpSource;
import com.github.linyuzai.download.okhttp.OkHttpSourceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支持 {@link OkHttpSource} 的配置。
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptCoreAutoConfiguration.class)
@ConditionalOnClass(OkHttpSourceFactory.class)
public class DownloadConceptSourceOkHttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(OkHttpSource.class)
    public OkHttpSourceFactory okHttpSourceFactory() {
        return new OkHttpSourceFactory();
    }
}
