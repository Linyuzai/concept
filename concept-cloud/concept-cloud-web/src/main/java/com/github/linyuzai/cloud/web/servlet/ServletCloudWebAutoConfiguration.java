package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletCloudWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebInterceptorChainFactory webInterceptorChainFactory() {
        return new ServletWebInterceptorChainFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ServletCloudWebAdvice servletCloudWebAdvice(WebContextFactory factory,
                                                       WebConcept concept) {
        return new ServletCloudWebAdvice(factory, concept);
    }
}
