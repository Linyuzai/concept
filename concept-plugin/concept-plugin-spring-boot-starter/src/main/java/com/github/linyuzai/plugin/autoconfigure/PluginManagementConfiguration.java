package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.management.ConditionalOnPluginManagementEnabled;
import com.github.linyuzai.plugin.autoconfigure.management.ReactivePluginManagementController;
import com.github.linyuzai.plugin.autoconfigure.management.ServletPluginManagementController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnPluginManagementEnabled
@Configuration(proxyBeanMethods = false)
public class PluginManagementConfiguration {

    @ConditionalOnPluginManagementEnabled
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Configuration(proxyBeanMethods = false)
    public static class WebMvcConfiguration implements WebMvcConfigurer {

        @Bean(initMethod = "init")
        public ServletPluginManagementController servletPluginManagementController() {
            return new ServletPluginManagementController();
        }

        @Override
        public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/concept-plugin/**")
                    .addResourceLocations("classpath:/concept/plugin/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }

    @ConditionalOnPluginManagementEnabled
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @Configuration(proxyBeanMethods = false)
    public static class WebFluxConfiguration implements WebFluxConfigurer {

        @Bean(initMethod = "init")
        public ReactivePluginManagementController reactivePluginManagementController() {
            return new ReactivePluginManagementController();
        }

        @Override
        public void addResourceHandlers(org.springframework.web.reactive.config.ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/concept-plugin/**")
                    .addResourceLocations("classpath:/concept/plugin/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}
