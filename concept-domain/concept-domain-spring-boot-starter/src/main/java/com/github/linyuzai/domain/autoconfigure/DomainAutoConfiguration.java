package com.github.linyuzai.domain.autoconfigure;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainEventPublisher;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.proxy.ProxyDomainFactory;
import com.github.linyuzai.domain.core.recycler.DomainRecycler;
import com.github.linyuzai.domain.core.recycler.LinkedDomainRecycler;
import com.github.linyuzai.domain.core.recycler.NotRecycledDomainRecycler;
import com.github.linyuzai.domain.core.recycler.ThreadLocalDomainRecycler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableConfigurationProperties(DomainProperties.class)
public class DomainAutoConfiguration {

    /**
     * 领域上下文
     */
    @Bean
    @ConditionalOnMissingBean
    public DomainContext domainContext(ApplicationContext context) {
        return new ApplicationDomainContext(context);
    }

    /**
     * 领域校验器
     */
    @Bean
    @ConditionalOnMissingBean
    public DomainValidator domainValidator(Validator validator) {
        return new ApplicationDomainValidator(validator);
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class DomainRecyclerConfiguration implements WebMvcConfigurer {

        @Autowired
        private DomainProperties properties;

        @Bean
        @ConditionalOnMissingBean
        public DomainRecycler domainRecycler() {
            DomainProperties.RecyclerProperties recycler = properties.getRecycler();
            if (recycler.isEnabled()) {
                if (recycler.isThreadLocalAutoRecycle()) {
                    return new ThreadLocalDomainRecycler(new LinkedDomainRecycler());
                } else {
                    return new LinkedDomainRecycler();
                }
            } else {
                return new NotRecycledDomainRecycler();
            }
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            DomainRecycler recycler = domainRecycler();
            if (recycler instanceof ThreadLocalDomainRecycler) {
                registry.addInterceptor(new AutoRecycleHandler((ThreadLocalDomainRecycler) recycler));
            }
        }

        @Getter
        @RequiredArgsConstructor
        public static class AutoRecycleHandler implements HandlerInterceptor {

            private final ThreadLocalDomainRecycler recycler;

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                recycler.recycle();
            }
        }
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public static class ReactiveDomainRecyclerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public DomainRecycler notRecycledDomainRecycler() {
            return new NotRecycledDomainRecycler();
        }
    }

    /**
     * 领域工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public DomainFactory domainFactory(DomainContext context, DomainRecycler recycler) {
        return new ProxyDomainFactory(context, recycler);
    }

    /**
     * 领域事件发布器
     */
    @Bean
    @ConditionalOnMissingBean
    public DomainEventPublisher domainEventPublisher(DomainContext context, ApplicationEventPublisher publisher) {
        return new ApplicationDomainEventPublisher(context, publisher);
    }
}
