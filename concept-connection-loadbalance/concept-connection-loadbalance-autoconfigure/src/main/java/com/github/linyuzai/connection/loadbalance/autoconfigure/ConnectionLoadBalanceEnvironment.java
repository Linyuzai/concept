package com.github.linyuzai.connection.loadbalance.autoconfigure;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.boot.web.reactive.context.ConfigurableReactiveWebEnvironment;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.WebApplicationContext;

/**
 * Env 支持类。
 * <p>
 * Deduce server type.
 */
@Getter
@Deprecated
public class ConnectionLoadBalanceEnvironment implements EnvironmentAware {

    private Environment environment;

    /**
     * 是否是 Servlet 环境
     *
     * @return 是否是 Servlet 环境
     */
    public boolean isServletWebApplication() {
        if (environment instanceof ConfigurableWebEnvironment) {
            return true;
        }
        if (environment instanceof WebApplicationContext) {
            return true;
        }
        return false;
    }

    /**
     * 是否是 Reactive 环境
     *
     * @return 是否是 Reactive 环境
     */
    public boolean isReactiveWebApplication() {
        if (environment instanceof ConfigurableReactiveWebEnvironment) {
            return true;
        }
        if (environment instanceof ReactiveWebApplicationContext) {
            return true;
        }
        return false;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}
