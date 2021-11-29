package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.info.DownloadConceptInfo;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.servlet.request.ServletDownloadRequestProvider;
import com.github.linyuzai.download.servlet.response.ServletDownloadResponseProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ServletDownloadRequestProvider.class, ServletDownloadResponseProvider.class})
public class DownloadConceptServletAutoConfiguration {

    //private static final Log logger = LogFactory.getLog(DownloadConceptServletAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "javax.servlet.http.HttpServletRequest")
    public DownloadRequestProvider downloadRequestProvider() {
        //logger.info(DownloadConceptInfo.wrapper("request provider => servlet"));
        return new ServletDownloadRequestProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "javax.servlet.http.HttpServletResponse")
    public DownloadResponseProvider downloadResponseProvider() {
        //logger.info(DownloadConceptInfo.wrapper("response provider => servlet"));
        return new ServletDownloadResponseProvider();
    }
}
