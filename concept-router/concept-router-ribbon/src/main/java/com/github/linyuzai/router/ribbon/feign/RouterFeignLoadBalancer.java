package com.github.linyuzai.router.ribbon.feign;

import com.netflix.client.ClientException;
import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerStats;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.servo.monitor.Timer;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;

import java.io.IOException;
import java.net.URI;

public class RouterFeignLoadBalancer extends FeignLoadBalancer {

    private final FeignLoadBalancer feignLoadBalancer;

    public RouterFeignLoadBalancer(FeignLoadBalancer feignLoadBalancer) {
        super(null, new DefaultClientConfigImpl(), null);
        this.feignLoadBalancer = feignLoadBalancer;
    }

    @Override
    protected void customizeLoadBalancerCommandBuilder(RibbonRequest request, IClientConfig config, LoadBalancerCommand.Builder<RibbonResponse> builder) {
        builder.withLoadBalancerContext(this);
    }

    @Override
    public Server getServerFromLoadBalancer(URI original, Object loadBalancerKey) throws ClientException {
        String name = getClientName();
        //TODO
        return super.getServerFromLoadBalancer(original, loadBalancerKey);
    }

    @Override
    public RibbonResponse execute(RibbonRequest request, IClientConfig configOverride) throws IOException {
        return feignLoadBalancer.execute(request, configOverride);
    }

    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(RibbonRequest request, IClientConfig requestConfig) {
        return feignLoadBalancer.getRequestSpecificRetryHandler(request, requestConfig);
    }

    @Override
    public URI reconstructURIWithServer(Server server, URI original) {
        return feignLoadBalancer.reconstructURIWithServer(server, original);
    }

    @Override
    public RibbonResponse executeWithLoadBalancer(RibbonRequest request) throws ClientException {
        return feignLoadBalancer.executeWithLoadBalancer(request);
    }

    @Override
    public RibbonResponse executeWithLoadBalancer(RibbonRequest request, IClientConfig requestConfig) throws ClientException {
        return feignLoadBalancer.executeWithLoadBalancer(request, requestConfig);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        feignLoadBalancer.initWithNiwsConfig(clientConfig);
    }

    @Override
    public Timer getExecuteTracer() {
        return feignLoadBalancer.getExecuteTracer();
    }

    @Override
    public String getClientName() {
        return feignLoadBalancer.getClientName();
    }

    @Override
    public ILoadBalancer getLoadBalancer() {
        return feignLoadBalancer.getLoadBalancer();
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        feignLoadBalancer.setLoadBalancer(lb);
    }

    @Override
    public int getMaxAutoRetriesNextServer() {
        return feignLoadBalancer.getMaxAutoRetriesNextServer();
    }

    @Override
    public void setMaxAutoRetriesNextServer(int maxAutoRetriesNextServer) {
        feignLoadBalancer.setMaxAutoRetriesNextServer(maxAutoRetriesNextServer);
    }

    @Override
    public int getMaxAutoRetries() {
        return feignLoadBalancer.getMaxAutoRetries();
    }

    @Override
    public void setMaxAutoRetries(int maxAutoRetries) {
        feignLoadBalancer.setMaxAutoRetries(maxAutoRetries);
    }

    @Override
    public void noteRequestCompletion(ServerStats stats, Object response, Throwable e, long responseTime, RetryHandler errorHandler) {
        feignLoadBalancer.noteRequestCompletion(stats, response, e, responseTime, errorHandler);
    }

    @Override
    public void noteOpenConnection(ServerStats serverStats) {
        feignLoadBalancer.noteOpenConnection(serverStats);
    }

    @Override
    public boolean handleSameServerRetry(Server server, int currentRetryCount, int maxRetries, Throwable e) {
        return feignLoadBalancer.handleSameServerRetry(server, currentRetryCount, maxRetries, e);
    }
}
