package com.github.linyuzai.cloud.web.core;

import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.concept.WebConceptConfigurer;
import com.github.linyuzai.cloud.web.core.concept.WebConceptImpl;
import com.github.linyuzai.cloud.web.core.i18n.*;
import com.github.linyuzai.cloud.web.core.i18n.condition.ConditionalOnWebI18nDisabled;
import com.github.linyuzai.cloud.web.core.i18n.condition.ConditionalOnWebI18nEnabled;
import com.github.linyuzai.cloud.web.core.i18n.result.I18nWebResultFactory;
import com.github.linyuzai.cloud.web.core.intercept.*;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import com.github.linyuzai.cloud.web.core.context.WebContextFactoryImpl;
import com.github.linyuzai.cloud.web.core.intercept.annotation.BreakInterceptMethodWebInterceptorFactory;
import com.github.linyuzai.cloud.web.core.intercept.annotation.SimpleMethodWebInterceptorFactory;
import com.github.linyuzai.cloud.web.core.intercept.annotation.WebInterceptorAnnotationBeanPostProcessor;
import com.github.linyuzai.cloud.web.core.intercept.condition.ConditionalOnWebInterceptionEnabled;
import com.github.linyuzai.cloud.web.core.intercept.condition.ConditionalOnWebRequestInterceptionEnabled;
import com.github.linyuzai.cloud.web.core.intercept.condition.ConditionalOnWebResponseInterceptionEnabled;
import com.github.linyuzai.cloud.web.core.result.BooleanWebResultFactory;
import com.github.linyuzai.cloud.web.core.result.WebResultFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;

/**
 * 核心配置
 */
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
    @AutoConfigureBefore(MessageSourceAutoConfiguration.class)
    @ConditionalOnWebI18nEnabled
    public static class I18nConfiguration {

        @Bean
        public MessageSourceBasename messageSourceBasename(ApplicationContext context) {
            return new DefaultMessageSourceBasename(context);
        }

        /**
         * i18n
         */
        @Bean
        public MessageSource messageSource(MessageSourceBasename basename, CloudWebProperties properties) {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

            CloudWebProperties.I18nProperties i18n = properties.getI18n();

            if (StringUtils.hasText(i18n.getBasename())) {
                messageSource.setBasenames(StringUtils
                        .commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(i18n.getBasename())));
            } else {
                messageSource.setBasenames(basename.get());
            }
            if (i18n.getEncoding() != null) {
                messageSource.setDefaultEncoding(i18n.getEncoding().name());
            }
            messageSource.setFallbackToSystemLocale(i18n.isFallbackToSystemLocale());
            Duration cacheDuration = i18n.getCacheDuration();
            if (cacheDuration != null) {
                messageSource.setCacheMillis(cacheDuration.toMillis());
            }
            messageSource.setAlwaysUseMessageFormat(i18n.isAlwaysUseMessageFormat());
            messageSource.setUseCodeAsDefaultMessage(i18n.isUseCodeAsDefaultMessage());

            return messageSource;
        }
    }

    @Configuration
    @AutoConfigureAfter(I18nConfiguration.class)
    @ConditionalOnWebInterceptionEnabled
    public static class InterceptConfiguration {

        @Bean
        public PropertiesInterceptorConfigurer propertiesInterceptorConfigurer(CloudWebProperties properties) {
            return new PropertiesInterceptorConfigurer(properties);
        }

        @Bean
        public SimpleMethodWebInterceptorFactory simpleMethodWebInterceptorFactory() {
            return new SimpleMethodWebInterceptorFactory();
        }

        @Bean
        public BreakInterceptMethodWebInterceptorFactory breakInterceptMethodWebInterceptorFactory() {
            return new BreakInterceptMethodWebInterceptorFactory();
        }

        @Bean
        public static WebInterceptorAnnotationBeanPostProcessor webInterceptorAnnotationBeanPostProcessor() {
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
            @ConditionalOnWebI18nEnabled
            public WebResultFactory i18nWebResultFactory() {
                return new I18nWebResultFactory();
            }

            @Bean
            @ConditionalOnMissingBean
            @ConditionalOnWebI18nDisabled
            public WebResultFactory webResultFactory() {
                return new BooleanWebResultFactory();
            }

            @Bean
            public LoggerErrorResponseInterceptor loggerErrorResponseInterceptor() {
                return new LoggerErrorResponseInterceptor();
            }

            @Bean
            public WebResultResponseInterceptor webResultResponseInterceptor(WebResultFactory webResultFactory) {
                return new WebResultResponseInterceptor(webResultFactory);
            }

            @Bean
            public StringTypeResponseInterceptor stringTypeResponseInterceptor() {
                return new StringTypeResponseInterceptor();
            }
        }
    }
}