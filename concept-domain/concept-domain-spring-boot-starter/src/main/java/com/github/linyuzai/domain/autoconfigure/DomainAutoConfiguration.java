package com.github.linyuzai.domain.autoconfigure;

import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainEventPublisher;
import com.github.linyuzai.domain.core.DomainFactory;
import com.github.linyuzai.domain.core.DomainValidator;
import com.github.linyuzai.domain.core.proxy.ProxyDomainFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

@Configuration
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

    /**
     * 领域工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public DomainFactory domainFactory(DomainContext context) {
        return new ProxyDomainFactory(context);
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
