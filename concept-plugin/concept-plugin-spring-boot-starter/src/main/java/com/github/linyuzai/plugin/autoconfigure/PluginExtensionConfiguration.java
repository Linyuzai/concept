package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.observable.ReactiveRequestMappingPluginObservable;
import com.github.linyuzai.plugin.autoconfigure.observable.ServletRequestMappingPluginObservable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "concept.plugin.extension.request-mapping.enabled", havingValue = "true")
public class PluginExtensionConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class ServletConfiguration {

        @Bean
        public ServletRequestMappingPluginObservable servletRequestMappingPluginObservable() {
            return new ServletRequestMappingPluginObservable();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public static class ReactiveConfiguration {

        @Bean
        public ReactiveRequestMappingPluginObservable reactiveRequestMappingPluginObservable() {
            return new ReactiveRequestMappingPluginObservable();
        }
    }
}
