package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.management.*;
import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 插件管理配置类
 */
@ConditionalOnPluginManagementEnabled
@Configuration(proxyBeanMethods = false)
public class PluginManagementConfiguration {

    @Bean(initMethod = "initialize")
    @ConditionalOnMissingBean
    public PluginManager pluginManager(PluginConceptProperties properties,
                                       PluginConcept concept,
                                       PluginStorage storage,
                                       PluginExecutor executor) {
        return new PluginManager(properties, concept, storage, executor);
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginManagementAuthorizer pluginManagementAuthorizer(PluginConceptProperties properties) {
        return new Base64PluginManagementAuthorizer(properties.getManagement().getAuthorization().getPassword());
    }

    @ConditionalOnPluginManagementEnabled
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Configuration(proxyBeanMethods = false)
    public static class WebMvcConfiguration implements WebMvcConfigurer {

        @Bean
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

        @Bean
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
