package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.annotation.ConditionalOnRouterManagementEnabled;
import com.github.linyuzai.router.autoconfigure.management.DefaultRouterConvertor;
import com.github.linyuzai.router.autoconfigure.management.RouterConvertor;
import com.github.linyuzai.router.autoconfigure.management.RouterManagementController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnRouterManagementEnabled
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

    @ConditionalOnRouterManagementEnabled
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Configuration(proxyBeanMethods = false)
    public static class WebMvcConfiguration implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/concept-router/**", "/concept/router/**")
                    .addResourceLocations("classpath:/concept/router/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }

    @ConditionalOnRouterManagementEnabled
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @Configuration(proxyBeanMethods = false)
    public static class WebFluxConfiguration implements WebFluxConfigurer {

        @Override
        public void addResourceHandlers(org.springframework.web.reactive.config.ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/concept-router/**", "/concept/router/**")
                    .addResourceLocations("classpath:/concept/router/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}
