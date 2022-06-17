package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.Router;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;

@Getter
@AllArgsConstructor
public class RequestRouterSource implements Router.Source {

    private String serviceId;

    private Request<RequestDataContext> request;
}
