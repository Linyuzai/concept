package com.github.linyuzai.cloud.web;

import com.github.linyuzai.cloud.web.core.CloudWebProperties;
import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.concept.WebConceptConfigurer;
import com.github.linyuzai.cloud.web.core.concept.WebConceptImpl;
import com.github.linyuzai.cloud.web.core.intercept.*;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import com.github.linyuzai.cloud.web.core.context.WebContextFactoryImpl;
import com.github.linyuzai.cloud.web.core.intercept.condition.ConditionalOnWebErrorInterceptionEnabled;
import com.github.linyuzai.cloud.web.core.intercept.condition.ConditionalOnWebInterceptionEnabled;
import com.github.linyuzai.cloud.web.core.intercept.condition.ConditionalOnWebRequestInterceptionEnabled;
import com.github.linyuzai.cloud.web.core.intercept.condition.ConditionalOnWebResponseInterceptionEnabled;
import com.github.linyuzai.cloud.web.core.result.BooleanWebResultFactory;
import com.github.linyuzai.cloud.web.core.result.WebResultFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(CloudWebProperties.class)
public class CloudWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebContextFactory webContextFactory() {
        return new WebContextFactoryImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebConcept webConcept(CloudWebProperties properties,
                                 WebInterceptorChainFactory factory,
                                 List<WebInterceptor> interceptors,
                                 List<WebConceptConfigurer> configurers) {
        WebConcept concept = new WebConceptImpl(properties, factory, interceptors);
        configurers.forEach(it -> it.configure(concept));
        return concept;
    }

    @Configuration
    @ConditionalOnWebInterceptionEnabled
    public static class InterceptConfiguration {

        @Bean
        @ConditionalOnMissingBean
        PropertiesInterceptorConfigurer propertiesInterceptorConfigurer(CloudWebProperties properties) {
            return new PropertiesInterceptorConfigurer(properties);
        }

        @Bean
        @ConditionalOnMissingBean
        public WebInterceptorAnnotationBeanPostProcessor webInterceptorAnnotationBeanPostProcessor() {
            return new WebInterceptorAnnotationBeanPostProcessor();
        }

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
            public WebResultResponseInterceptor webResultResponseInterceptor(WebResultFactory webResultFactory) {
                return new WebResultResponseInterceptor(webResultFactory);
            }

            @Bean
            @ConditionalOnMissingBean
            public StringTypeResponseInterceptor stringTypeResponseInterceptor() {
                return new StringTypeResponseInterceptor();
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
    }
}