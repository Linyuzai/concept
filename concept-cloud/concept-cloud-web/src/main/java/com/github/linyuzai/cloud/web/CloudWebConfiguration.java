package com.github.linyuzai.cloud.web;

import com.github.linyuzai.cloud.web.context.WebContextFactory;
import com.github.linyuzai.cloud.web.context.WebContextFactoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CloudWebConfiguration.class)
public class CloudWebConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebContextFactory webContextFactory() {
        return new WebContextFactoryImpl();
    }

    /*@Configuration
    @ConditionalOnWebInterceptEnabled
    public static class InterceptConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public WebInterceptorChainFactory webInterceptorChainFactory() {
            return new WebInterceptorChainFactoryImpl();
        }

        @Configuration
        @ConditionalOnRequestInterceptEnabled
        public static class RequestConfiguration {

            @Bean
            @ConditionalOnProperty(name = "concept.cloud.web.intercept.request.skip.urls")
            public UriSkipRequestInterceptor uriSkipRequestInterceptor(WebConceptProperties properties) {
                return new UriSkipRequestInterceptor(properties.getIntercept().getRequest().getSkip().getUrls());
            }
        }

        @Configuration
        @ConditionalOnResponseInterceptEnabled
        public static class ResponseConfiguration {

            @Bean
            @ConditionalOnProperty(name = "concept.cloud.web.intercept.response.skip.urls")
            public UriSkipResponseInterceptor uriSkipResponseInterceptor(WebConceptProperties properties) {
                return new UriSkipResponseInterceptor(properties.getIntercept().getResponse().getSkip().getUrls());
            }

            @Bean
            public ResultMessageResponseInterceptor resultMessageResponseInterceptor() {
                return new ResultMessageResponseInterceptor();
            }

            @Bean
            public ResponseWebResultFactory responseWebResultFactory() {
                return new ResponseWebResultFactory();
            }

            @Bean
            @ConditionalOnMissingBean
            public WebResultFactoryAdapter webResultFactoryAdapter(List<WebResultFactory> webResultFactories) {
                return new WebResultFactoryAdapterImpl(webResultFactories);
            }

            /**
             * 加载响应WrapResult拦截器
             *
            @Bean
            public WrapResultResponseInterceptor wrapResultResponseInterceptor(WebResultFactoryAdapter webResultFactoryAdapter) {
                return new WrapResultResponseInterceptor(webResultFactoryAdapter);
            }

            /**
             * 加载响应String拦截器
             *
            @Bean
            public StringTypeResponseInterceptor stringTypeResponseInterceptor() {
                return new StringTypeResponseInterceptor();
            }
        }

        @Configuration
        @ConditionalOnErrorInterceptEnabled
        public static class ErrorConfiguration {

            @Bean
            public LoggerErrorInterceptor loggerErrorInterceptor() {
                return new LoggerErrorInterceptor();
            }

            @Bean
            public ExceptionResultErrorInterceptor exceptionResultErrorInterceptor() {
                return new ExceptionResultErrorInterceptor();
            }
        }

        @Bean
        public RestControllerAndResponseBodyAdvice restControllerAndResponseBodyAdvice(WebConceptProperties properties,
                                                                                       WebContextFactory contextFactory,
                                                                                       WebInterceptorChainFactory chainFactory,
                                                                                       List<WebInterceptor> interceptors) {
            return new RestControllerAndResponseBodyAdvice(properties, contextFactory, chainFactory, interceptors);
        }

        @Configuration
        public static class SwaggerConfiguration {

            @Configuration
            public static class SpringFoxConfiguration {

            }

            @Configuration
            @ConditionalOnClass(name = "org.springdoc.core.OpenAPIService")
            @ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
            public static class SpringDocConfiguration {

                @Bean
                @ConditionalOnRequestInterceptEnabled
                @ConditionalOnProperty(name = "concept.cloud.web.intercept.request.skip.swagger", havingValue = "true", matchIfMissing = true)
                public UriSkipRequestInterceptor swaggerSkipRequestInterceptor() {
                    return new UriSkipRequestInterceptor(SWAGGER_SPRING_DOC_PATTERN);
                }

                @Bean
                @ConditionalOnResponseInterceptEnabled
                @ConditionalOnProperty(name = "concept.cloud.web.intercept.response.skip.swagger", havingValue = "true", matchIfMissing = true)
                public UriSkipResponseInterceptor swaggerSkipResponseInterceptor() {
                    return new UriSkipResponseInterceptor(SWAGGER_SPRING_DOC_PATTERN);
                }
            }
        }
    }*/
}
