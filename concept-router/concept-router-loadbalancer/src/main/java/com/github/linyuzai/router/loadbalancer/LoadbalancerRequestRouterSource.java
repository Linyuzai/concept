package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.RequestRouterSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;

import java.net.URI;

@Getter
@AllArgsConstructor
public class LoadbalancerRequestRouterSource implements RequestRouterSource {

    private String serviceId;

    private Request<RequestDataContext> request;

    @Override
    public URI getUri() {
        return request.getContext().getClientRequest().getUrl();
    }
}
