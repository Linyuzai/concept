package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.management.*;
import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 插件管理配置类
 */
@ConditionalOnPluginManagementEnabled
@Configuration(proxyBeanMethods = false)
public class PluginManagementConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PluginManagementAuthorizer pluginManagementAuthorizer(PluginConceptProperties properties) {
        return new Base64PluginManagementAuthorizer(properties.getManagement().getAuthorization().getPassword());
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginPropertiesProvider pluginPropertiesProvider(PluginLocation location,
                                                             PluginConcept concept,
                                                             List<PluginFactory> factories) {
        return new DefaultPluginPropertiesProvider(location, concept, factories);
    }

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
