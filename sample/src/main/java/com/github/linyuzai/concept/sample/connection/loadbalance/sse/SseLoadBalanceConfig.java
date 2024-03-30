/*
package com.github.linyuzai.concept.sample.connection.loadbalance.sse;

import com.github.linyuzai.connection.loadbalance.sse.EnableSseLoadBalanceConcept;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableSseLoadBalanceConcept
public class SseLoadBalanceConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/concept-sse/**");
            }
        };
    }

}
*/
