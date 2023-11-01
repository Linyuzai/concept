package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.web.servlet.ServletDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Servlet 环境配置。
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DownloadConceptServletAutoConfiguration {

    @Bean
    public DownloadConcept downloadConcept(DownloadContextFactory factory,
                                           List<DownloadHandler> handlers) {
        return new ServletDownloadConcept(factory, handlers);
    }
}
