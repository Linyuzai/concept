package $PACKAGE$.basic.cloud;

import $PACKAGE$.basic.cloud.register.MetadataRegister;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

/**
 * 微服务需要注入的组件
 */
@Configuration
public class $CLASS$CloudConfiguration {

    @Bean
    public MetadataRegister metadataRegister(List<GroupedOpenApi> groupedOpenApis) {
        return new MetadataRegister(groupedOpenApis);
    }

    @Bean
    public CorsFilter corsFilter() {
        //主要是为了网关聚合API的请求跨域问题，可以只允许网关地址
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
