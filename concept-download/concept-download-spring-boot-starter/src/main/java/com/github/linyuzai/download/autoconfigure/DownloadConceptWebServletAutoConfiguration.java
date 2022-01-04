package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.web.servlet.request.ServletDownloadRequestProvider;
import com.github.linyuzai.download.web.servlet.response.ServletDownloadResponseProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet的配置 / Configuration of servlet
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({ServletDownloadRequestProvider.class, ServletDownloadResponseProvider.class})
public class DownloadConceptWebServletAutoConfiguration {

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
}