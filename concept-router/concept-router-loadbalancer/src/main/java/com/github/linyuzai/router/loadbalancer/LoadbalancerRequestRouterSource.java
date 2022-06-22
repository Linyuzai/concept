package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.RequestRouterSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;

import java.net.URI;

@Getter
@AllArgsConstructor
public class LoadbalancerRequestRouterSource implements RequestRouterSource {

    @NonNull
    private String serviceId;

    @NonNull
    private Request<RequestDataContext> request;

    @Override
    public URI getUri() {
        return request.getContext().getClientRequest().getUrl();
    }
}
