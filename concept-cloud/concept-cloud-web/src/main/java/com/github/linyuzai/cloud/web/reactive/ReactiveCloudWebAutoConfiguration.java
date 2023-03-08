package com.github.linyuzai.cloud.web.reactive;

import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.context.WebContextFactory;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChainFactory;
import com.github.linyuzai.cloud.web.servlet.ServletCloudWebAdvice;
import com.github.linyuzai.cloud.web.servlet.ServletWebInterceptorChainFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveCloudWebAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebInterceptorChainFactory webInterceptorChainFactory() {
        return new ReactiveWebInterceptorChainFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactiveCloudWebAdvice reactiveCloudWebAdvice(ServerCodecConfigurer serverCodecConfigurer,
                                                         @Qualifier("webFluxContentTypeResolver") RequestedContentTypeResolver contentTypeResolver,
                                                         @Qualifier("webFluxAdapterRegistry") ReactiveAdapterRegistry reactiveAdapterRegistry,
                                                         WebContextFactory factory,
                                                         WebConcept concept) {
        return new ReactiveCloudWebAdvice(serverCodecConfigurer.getWriters(),
                contentTypeResolver, reactiveAdapterRegistry, factory, concept);
    }
}
