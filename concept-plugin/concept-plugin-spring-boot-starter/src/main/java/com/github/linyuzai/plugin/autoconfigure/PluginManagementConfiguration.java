package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.management.ConditionalOnPluginManagementEnabled;
import com.github.linyuzai.plugin.autoconfigure.management.PluginManagementController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnPluginManagementEnabled
@Configuration(proxyBeanMethods = false)
public class PluginManagementConfiguration {

    @Bean
    public PluginManagementController pluginManagementController() {
        return new PluginManagementController();
    }

    @ConditionalOnPluginManagementEnabled
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Configuration(proxyBeanMethods = false)
    public static class WebMvcConfiguration implements WebMvcConfigurer {

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

        @Override
        public void addResourceHandlers(org.springframework.web.reactive.config.ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/concept-plugin/**")
                    .addResourceLocations("classpath:/concept/plugin/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}
