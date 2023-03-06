package com.github.linyuzai.cloud.web;

import com.github.linyuzai.cloud.web.core.CloudWebProperties;
import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.concept.WebConceptImpl;
import com.github.linyuzai.cloud.web.core.intercept.*;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import com.github.linyuzai.cloud.web.core.context.WebContextFactoryImpl;
import com.github.linyuzai.cloud.web.core.result.BooleanWebResultFactory;
import com.github.linyuzai.cloud.web.core.result.WebResultFactory;
import com.github.linyuzai.cloud.web.servlet.RestControllerAndResponseBodyAdvice;
import com.github.linyuzai.cloud.web.servlet.ServletWebInterceptorChainFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(CloudWebProperties.class)
public class CloudWebConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebContextFactory webContextFactory() {
        return new WebContextFactoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebConcept webConcept(CloudWebProperties properties,
                                 WebInterceptorChainFactory factory,
                                 List<WebInterceptor> interceptors) {
        return new WebConceptImpl(properties, factory, interceptors);
    }

    @Configuration
    @ConditionalOnWebInterceptionEnabled
    public static class InterceptConfiguration {

        @Configuration
        @ConditionalOnWebRequestInterceptionEnabled
        public static class RequestConfiguration {

        }

        @Configuration
        @ConditionalOnWebResponseInterceptionEnabled
        public static class ResponseConfiguration {

            @Bean
            @ConditionalOnMissingBean
            public WebResultFactory webResultFactory() {
                return new BooleanWebResultFactory();
            }

            @Bean
            @ConditionalOnMissingBean
            public CreateWebResultResponseInterceptor createWebResultResponseInterceptor(WebResultFactory webResultFactory) {
                return new CreateWebResultResponseInterceptor(webResultFactory);
            }

            @Bean
            @ConditionalOnMissingBean
            public ParseStringTypeResponseInterceptor parseStringTypeResponseInterceptor() {
                return new ParseStringTypeResponseInterceptor();
            }
        }

        @Configuration
        @ConditionalOnWebErrorInterceptionEnabled
        public static class ErrorConfiguration {

            @Bean
            @ConditionalOnMissingBean
            public LoggerErrorInterceptor loggerErrorInterceptor() {
                return new LoggerErrorInterceptor();
            }
        }

        @Configuration
        @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
        public static class ServletConfiguration {

            @Bean
            @ConditionalOnMissingBean
            public WebInterceptorChainFactory webInterceptorChainFactory() {
                return new ServletWebInterceptorChainFactory();
            }

            @Bean
            @ConditionalOnMissingBean
            public RestControllerAndResponseBodyAdvice restControllerAndResponseBodyAdvice(WebContextFactory factory,
                                                                                           WebConcept concept) {
                return new RestControllerAndResponseBodyAdvice(factory, concept);
            }
        }
    }
}