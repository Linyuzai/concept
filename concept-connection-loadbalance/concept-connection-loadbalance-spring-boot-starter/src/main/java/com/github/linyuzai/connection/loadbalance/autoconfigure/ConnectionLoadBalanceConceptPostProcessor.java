package com.github.linyuzai.connection.loadbalance.autoconfigure;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ConnectionLoadBalanceConceptPostProcessor implements BeanPostProcessor {



    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {

        return bean;
    }
}
