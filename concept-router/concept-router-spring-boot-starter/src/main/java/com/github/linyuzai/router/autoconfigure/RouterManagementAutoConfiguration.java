package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.management.DefaultRouterConvertor;
import com.github.linyuzai.router.autoconfigure.management.RouterConvertor;
import com.github.linyuzai.router.autoconfigure.management.RouterManagementController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
