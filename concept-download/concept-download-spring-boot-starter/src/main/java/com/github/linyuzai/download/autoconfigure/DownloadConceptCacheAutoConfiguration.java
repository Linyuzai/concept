package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.cache.DownloadCacheLocation;
import com.github.linyuzai.download.core.cache.UserHomeDownloadCacheLocation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DownloadConceptCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DownloadCacheLocation downloadCacheLocation() {
        return new UserHomeDownloadCacheLocation();
    }
}
