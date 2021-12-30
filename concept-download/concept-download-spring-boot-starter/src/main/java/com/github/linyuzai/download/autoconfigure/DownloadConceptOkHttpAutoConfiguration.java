package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.okhttp.OkHttpSourceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OkHttp的配置 / Configuration of OkHttp
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptAutoConfiguration.class)
@ConditionalOnClass(OkHttpSourceFactory.class)
public class DownloadConceptOkHttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "okhttp3.OkHttpClient")
    public OkHttpSourceFactory okHttpOriginalSourceFactory() {
        return new OkHttpSourceFactory();
    }
}
