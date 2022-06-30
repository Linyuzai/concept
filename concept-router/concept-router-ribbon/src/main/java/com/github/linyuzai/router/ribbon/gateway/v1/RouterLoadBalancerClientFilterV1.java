package com.github.linyuzai.router.ribbon.gateway.v1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * 低版本的网关由于没有可以重写的方法所以全部复制过来
 * <p>
 * 该代码为 spring boot 2.2.5
 */
public class RouterLoadBalancerClientFilterV1 implements GlobalFilter, Ordered {
    private static final Log log = LogFactory.getLog(LoadBalancerClientFilter.class);
    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10100;
    private final LoadBalancerClient loadBalancer;

    private final ApplicationContext context;

    public RouterLoadBalancerClientFilterV1(ApplicationContext context) {
        this.loadBalancer = context.getBean(LoadBalancerClient.class);
        this.context = context;
    }

    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null || !"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix)) {
            return chain.filter(exchange);
        } else {
            ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);
            log.trace("LoadBalancerClientFilter url before: " + url);
            ServiceInstance instance = choose(exchange);
            if (instance == null) {
                throw new NotFoundException("Unable to find instance for " + url.getHost());
            } else {
                URI uri = exchange.getRequest().getURI();
                String overrideScheme = null;
                if (schemePrefix != null) {
                    overrideScheme = url.getScheme();
                }

                URI requestUrl = this.loadBalancer.reconstructURI(new DelegatingServiceInstance(instance, overrideScheme), uri);
                log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
                return chain.filter(exchange);
            }
        }
    }

    /**
     * 将选择服务单独抽取出来
     * <p>
     * 调用 {@link RibbonLoadBalancerClient#choose(String, Object)} 方法
     */
    protected ServiceInstance choose(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String serviceId = Objects.requireNonNull(uri).getHost();
        if (loadBalancer instanceof RibbonLoadBalancerClient) {
            return new RouterLoadBalancerClient(context, uri).choose(serviceId);
        } else {
            return loadBalancer.choose(serviceId);
        }
    }

    class DelegatingServiceInstance implements ServiceInstance {
        final ServiceInstance delegate;
        private String overrideScheme;

        DelegatingServiceInstance(ServiceInstance delegate, String overrideScheme) {
            this.delegate = delegate;
            this.overrideScheme = overrideScheme;
        }

        public String getServiceId() {
            return this.delegate.getServiceId();
        }

        public String getHost() {
            return this.delegate.getHost();
        }

        public int getPort() {
            return this.delegate.getPort();
        }

        public boolean isSecure() {
            return this.delegate.isSecure();
        }

        public URI getUri() {
            return this.delegate.getUri();
        }

        public Map<String, String> getMetadata() {
            return this.delegate.getMetadata();
        }

        public String getScheme() {
            String scheme = this.delegate.getScheme();
            return scheme != null ? scheme : this.overrideScheme;
        }
    }
}
