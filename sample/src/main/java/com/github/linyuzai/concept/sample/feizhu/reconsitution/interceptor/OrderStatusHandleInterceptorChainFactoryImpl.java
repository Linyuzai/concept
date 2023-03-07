package com.github.linyuzai.concept.sample.feizhu.reconsitution.interceptor;

import com.github.linyuzai.concept.sample.feizhu.reconsitution.status.OrderStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderStatusHandleInterceptorChainFactoryImpl implements OrderStatusHandleInterceptorChainFactory {

    private final List<OrderStatusHandleInterceptor> interceptors;

    @Override
    public OrderStatusHandleInterceptorChain create(OrderStatus status) {
        List<OrderStatusHandleInterceptor> list = interceptors.stream()
                .filter(it -> it.support(status))
                .collect(Collectors.toList());
        return new OrderStatusHandleInterceptorChainImpl(0, list);
    }
}
