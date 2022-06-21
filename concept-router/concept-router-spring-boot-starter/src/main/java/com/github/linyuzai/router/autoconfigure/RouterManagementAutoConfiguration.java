package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.management.DefaultRouterConvertor;
import com.github.linyuzai.router.autoconfigure.management.RouterConvertor;
import com.github.linyuzai.router.autoconfigure.management.RouterManagementController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnProperty(name = {
        "concept.router.enabled",
        "concept.router.management.enabled"},
        havingValue = "true",
        matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
public class RouterManagementAutoConfiguration {

    @Bean
    public RouterConvertor routerConvertor() {
        return new DefaultRouterConvertor();
    }

    @Bean
    public RouterManagementController routerManagementController() {
        return new RouterManagementController();
    }

    @ConditionalOnProperty(name = {
            "concept.router.enabled",
            "concept.router.management.enabled"},
            havingValue = "true",
            matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Configuration(proxyBeanMethods = false)
    public static class WebMvcRouterManagementAutoConfiguration implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/concept-router/**")
                    .addResourceLocations("classpath:/concept/router/");
        }
    }

    @ConditionalOnProperty(name = {
            "concept.router.enabled",
            "concept.router.management.enabled"},
            havingValue = "true",
            matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @Configuration(proxyBeanMethods = false)
    public static class WebFluxRouterManagementAutoConfiguration implements WebFluxConfigurer {

        @Override
        public void addResourceHandlers(org.springframework.web.reactive.config.ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/concept-router/**")
                    .addResourceLocations("classpath:/concept/router/");
        }
    }
}
