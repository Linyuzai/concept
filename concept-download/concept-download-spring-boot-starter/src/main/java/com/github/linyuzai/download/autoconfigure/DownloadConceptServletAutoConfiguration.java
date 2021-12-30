package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.servlet.request.ServletDownloadRequestProvider;
import com.github.linyuzai.download.servlet.response.ServletDownloadResponseProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet的配置 / Configuration of servlet
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptAutoConfiguration.class)
@ConditionalOnClass({ServletDownloadRequestProvider.class, ServletDownloadResponseProvider.class})
public class DownloadConceptServletAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "javax.servlet.http.HttpServletRequest")
    public DownloadRequestProvider downloadRequestProvider() {
        return new ServletDownloadRequestProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "javax.servlet.http.HttpServletResponse")
    public DownloadResponseProvider downloadResponseProvider() {
        return new ServletDownloadResponseProvider();
    }
}
