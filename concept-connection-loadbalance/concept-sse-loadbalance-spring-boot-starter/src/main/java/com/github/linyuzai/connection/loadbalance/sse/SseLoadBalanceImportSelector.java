package com.github.linyuzai.connection.loadbalance.sse;

import com.github.linyuzai.connection.loadbalance.autoconfigure.ConnectionLoadBalanceEnvironment;
import com.github.linyuzai.connection.loadbalance.sse.reactive.ReactiveSseLoadBalanceConfiguration;
import com.github.linyuzai.connection.loadbalance.sse.servlet.ServletSseLoadBalanceConfiguration;
import lombok.NonNull;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * SSE 负载均衡的导入选择器。
 * <p>
 * Import selector for SSE load balancing.
 */
public class SseLoadBalanceImportSelector extends ConnectionLoadBalanceEnvironment implements ImportSelector {

    @Override
    public String @NonNull [] selectImports(@NonNull AnnotationMetadata metadata) {
        return new String[]{getConfigurationClass().getName()};
    }

    private Class<?> getConfigurationClass() {
        if (isServletWebApplication()) {
            return ServletSseLoadBalanceConfiguration.class;
        } else if (isReactiveWebApplication()) {
            return ReactiveSseLoadBalanceConfiguration.class;
        }
        throw new IllegalArgumentException("SSE type can not deduce");
    }
}
