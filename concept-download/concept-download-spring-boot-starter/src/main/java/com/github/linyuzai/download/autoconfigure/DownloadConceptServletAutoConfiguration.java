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

@Configuration
@AutoConfigureBefore(DownloadConceptAutoConfiguration.class)
@ConditionalOnClass({ServletDownloadRequestProvider.class, ServletDownloadResponseProvider.class})
public class DownloadConceptServletAutoConfiguration {

    //private static final Log logger = LogFactory.getLog(DownloadConceptServletAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(DownloadRequestProvider.class)
    @ConditionalOnClass(name = "javax.servlet.http.HttpServletRequest")
    public ServletDownloadRequestProvider downloadRequestProvider() {
        return new ServletDownloadRequestProvider();
    }

    @Bean
    @ConditionalOnMissingBean(DownloadResponseProvider.class)
    @ConditionalOnClass(name = "javax.servlet.http.HttpServletResponse")
    public ServletDownloadResponseProvider downloadResponseProvider() {
        return new ServletDownloadResponseProvider();
    }
}
