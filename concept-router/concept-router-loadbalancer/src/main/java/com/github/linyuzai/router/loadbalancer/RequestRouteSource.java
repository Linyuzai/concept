package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;

@Getter
@AllArgsConstructor
public class RequestRouteSource implements Route.Source {

    private String serviceId;

    private Request<RequestDataContext> request;
}
