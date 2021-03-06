package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.concept.DownloadReturnInterceptor;
import com.github.linyuzai.download.core.web.DownloadRequestProvider;
import com.github.linyuzai.download.core.web.DownloadResponseProvider;
import com.github.linyuzai.download.core.web.servlet.ServletDownloadRequestProvider;
import com.github.linyuzai.download.core.web.servlet.ServletDownloadResponseProvider;
import com.github.linyuzai.download.core.web.servlet.ServletDownloadReturnInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet 环境配置。
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptCoreAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DownloadConceptServletAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DownloadRequestProvider downloadRequestProvider() {
        return new ServletDownloadRequestProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadResponseProvider downloadResponseProvider() {
        return new ServletDownloadResponseProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public DownloadReturnInterceptor downloadReturnInterceptor() {
        return new ServletDownloadReturnInterceptor();
    }
}
