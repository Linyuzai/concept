package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.cache.DownloadCacheLocation;
import com.github.linyuzai.download.okhttp.OkHttpOriginalSourceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(DownloadConceptAutoConfiguration.class)
@AutoConfigureAfter(DownloadConceptCacheAutoConfiguration.class)
@ConditionalOnClass(OkHttpOriginalSourceFactory.class)
public class DownloadConceptOkHttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "okhttp3.OkHttpClient")
    public OkHttpOriginalSourceFactory okHttpOriginalSourceFactory(DownloadCacheLocation cacheLocation) {
        return new OkHttpOriginalSourceFactory(cacheLocation);
    }
}
